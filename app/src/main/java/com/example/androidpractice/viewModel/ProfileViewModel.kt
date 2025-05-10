package com.example.androidpractice.viewModel

import android.app.AlarmManager
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.androidpractice.content.Screen
import com.example.androidpractice.content.notification.NotificationReceiver
import com.example.androidpractice.domain.model.Profile
import com.example.androidpractice.domain.repository.IProfileRepository
import com.example.androidpractice.state.ProfileState
import com.example.androidpractice.utils.NotificationUtils
import com.example.androidpractice.utils.launchLoadingAndError
import org.koin.java.KoinJavaComponent
import java.io.File
import java.util.Calendar
import java.util.Date

class ProfileViewModel(
    private val repository: IProfileRepository,
    private val navigation: NavHostController
) : ViewModel() {
    private val mutableState = MutableProfileState()
    val viewState = mutableState as ProfileState
    private val context: Context by KoinJavaComponent.inject(Context::class.java)

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launchLoadingAndError(
            handleError = { mutableState.error = it.localizedMessage },
            updateLoading = { mutableState.isLoading = it }
        ) {
            repository.getProfile()?.let { profile ->
                updateState(profile)
            }
        }
    }

    fun onEditProfileClicked() {
        navigation.navigate(route = Screen.EditProfile.route)
    }

    fun onDocumentClick() {
        if (viewState.resumeUrl.isBlank()) {
            mutableState.error = "URL резюме не указан"
            return
        }
        try {
            downloadDocument(context, viewState.resumeUrl)
        } catch (e: Exception) {
            mutableState.error = "Ошибка при загрузке документа: ${e.message}"
        }
    }

    private fun downloadDocument(context: Context, url: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(url.toUri())

        val fileExtension = url.substringAfterLast('.', "pdf")
        val filename = "resume_${Date().time}.$fileExtension"

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("Загрузка резюме")
        request.setDescription("Загрузка файла резюме")

        val downloadId = downloadManager.enqueue(request)
        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    try {
                        onDownloadComplete(context, filename)
                    } catch (e: Exception) {
                        mutableState.error = "Ошибка при открытии документа: ${e.message}"
                    } finally {
                        context.unregisterReceiver(this)
                    }
                }
            }
        }

        ContextCompat.registerReceiver(
            context,
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    private fun onDownloadComplete(context: Context, filename: String) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            filename
        )

        if (!file.exists()) {
            throw IllegalStateException("Файл не найден")
        }

        val mimeType = getMimeType(filename)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val resolveInfo = context.packageManager.queryIntentActivities(intent, 0)
        if (resolveInfo.isNotEmpty()) {
            context.startActivity(intent)
        } else {
            val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "*/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (fallbackIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(fallbackIntent)
            } else {
                throw IllegalStateException("Нет приложения для открытия файла")
            }
        }
    }

    private fun getMimeType(filename: String): String {
        return when (filename.substringAfterLast('.').lowercase()) {
            "pdf" -> "application/pdf"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "txt" -> "text/plain"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> "application/octet-stream"
        }
    }

    fun onSaveProfile(profile: Profile) {
        viewModelScope.launchLoadingAndError(
            handleError = { mutableState.error = it.localizedMessage },
            updateLoading = { mutableState.isLoading = it }
        ) {
            val profileSaved = repository.setProfile(profile = profile)
            updateState(profileSaved)
            if (profile.favoriteClassTime.isNotEmpty()) {
                scheduleClassNotification(profile.favoriteClassTime, profile.fio)
            }

            navigation.navigate(route = Screen.Profile.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    private fun scheduleClassNotification(time: String, name: String) {
        try {
            if (!NotificationUtils.hasNotificationPermission(context)) {
                mutableState.error = "Требуется разрешение на отправку уведомлений"
                return
            }

            if (!NotificationUtils.areNotificationsEnabled(context)) {
                mutableState.error = "Уведомления отключены в настройках системы"
                return
            }

            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val (hour, minute) = time.split(":").map { it.toInt() }

            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                }
            }

            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("profile_name", name)
                action = "com.example.androidpractice.NOTIFICATION"
            }

            val pi = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmMgr.canScheduleExactAlarms()) {
                    Log.w(
                        "ProfileViewModel",
                        "Cannot schedule exact alarms, using setAndAllowWhileIdle"
                    )
                    alarmMgr.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pi
                    )
                } else {
                    alarmMgr.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pi
                    )
                }
            } else {
                alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    cal.timeInMillis,
                    pi
                )
            }

            Log.d("ProfileViewModel", "Notification scheduled for ${cal.time}")
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error scheduling notification", e)
            mutableState.error = "Ошибка при установке уведомления: ${e.message}"
        }
    }


    private fun updateState(profile: Profile) {
        mutableState.fio = profile.fio
        mutableState.avatarUri = profile.avatarUri
        mutableState.resumeUrl = profile.resumeUrl
        mutableState.position = profile.position
        mutableState.email = profile.email
        mutableState.favoriteClassTime = profile.favoriteClassTime
    }

    class MutableProfileState : ProfileState {
        override var fio: String by mutableStateOf("")
        override var avatarUri: String by mutableStateOf("")
        override var resumeUrl: String by mutableStateOf("")
        override var position: String by mutableStateOf("")
        override var email: String by mutableStateOf("")
        override var favoriteClassTime: String by mutableStateOf("")
        override var isLoading: Boolean by mutableStateOf(false)
        override var error: String? by mutableStateOf(null)
    }
}