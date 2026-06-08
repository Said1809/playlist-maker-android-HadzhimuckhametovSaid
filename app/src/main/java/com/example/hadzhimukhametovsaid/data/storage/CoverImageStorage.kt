package com.example.hadzhimukhametovsaid.data.storage

import android.content.Context
import android.net.Uri
import java.io.File

class CoverImageStorage(private val context: Context) {

    private val coversDir: File
        get() = File(context.filesDir, "covers").also { it.mkdirs() }

    fun copyToLocalStorage(sourceUri: String, playlistId: Long): String? {
        return try {
            val destFile = coverFile(playlistId)
            context.contentResolver.openInputStream(Uri.parse(sourceUri))?.use { input ->
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            destFile.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun deleteCover(playlistId: Long) {
        coverFile(playlistId).delete()
    }

    private fun coverFile(playlistId: Long): File =
        File(coversDir, "playlist_$playlistId.jpg")
}
