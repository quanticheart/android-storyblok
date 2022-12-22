@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.quanticheart.storyblok.ui.email.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.*
import java.net.URL
import java.util.regex.Pattern
import kotlin.concurrent.thread

//
// Created by Jonn Alves on 20/12/22.
//
class MarkdownView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : WebView(context, attrs, defStyleAttr) {

    //region Global Variables
    private val tag = "MarkDown"

    /**
     * Creates a composable view that can be used to display Markdown text.
     * @param text - The text used to generate the Markdown.
     * @param shouldOpenUrlInBrowser - Flag that tells the composable if it needs to open urls in a the browser or in the same view.
     */
    fun setMarkdown(
        text: String,
        shouldOpenUrlInBrowser: Boolean = true
    ) {
        val bs64MdText: String = imgToBase64(text)
        val escMdText: String = escapeForText(bs64MdText)
        val previewText = "preview('$escMdText')"

//        layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view?.evaluateJavascript(previewText, null)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (shouldOpenUrlInBrowser) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, request?.url))
                    return true
                }
                return false
            }
        }
        loadUrl("file:///android_asset/html/preview.html")
        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }

    /**
     * Creates a composable view that can be used to display Markdown from a file.
     * @param file - The file which is used to get the details to generate the Markdown.
     * @param shouldOpenUrlInBrowser - Flag that tells the composable if it needs to open urls in a the browser or in the same view.
     */
    fun setMarkdown(
        file: File,
        shouldOpenUrlInBrowser: Boolean = true
    ) {
        var mdText = ""
        try {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var readText: String?
            val stringBuilder = StringBuilder()
            while (bufferedReader.readLine().also { readText = it } != null) {
                stringBuilder.append(readText)
                stringBuilder.append("\n")
            }
            fileInputStream.close()
            mdText = stringBuilder.toString()
        } catch (e: FileNotFoundException) {
            e.message?.let { Log.d(tag, it) }
        } catch (e: IOException) {
            e.message?.let { Log.d(tag, it) }
        }
        setMarkdown(
            text = mdText,
            shouldOpenUrlInBrowser = shouldOpenUrlInBrowser
        )
    }

    /**
     * Creates a composable view that can be used to display Markdown from a url.
     * @param url - The url to the Markdown file.
     * @param shouldOpenUrlInBrowser - Flag that tells the composable if it needs to open urls in a the browser or in the same view.
     */
    fun setMarkdown(
        url: URL,
        shouldOpenUrlInBrowser: Boolean = true
    ) {
        var urlContent = ""
        thread {
            val reader = BufferedReader(InputStreamReader(url.openStream()))
            var line: String
            var mdText = ""
            while (reader.readLine().also { line = it ?: "" } != null) {
                mdText += "$line\n"
            }
            reader.close()
            urlContent = mdText
        }
        if (urlContent.isNotEmpty()) {
            setMarkdown(
                text = urlContent,
                shouldOpenUrlInBrowser = shouldOpenUrlInBrowser
            )
        }
    }

    private fun escapeForText(mdText: String): String {
        var escText = mdText.replace("\n", "\\\\n")
        escText = escText.replace("'", "\\\'")
        escText = escText.replace("\r", "")
        return escText
    }

    private fun imgToBase64(mdText: String): String {
        val ptn = Pattern.compile("!\\[(.*)]\\((.*)\\)")
        val matcher = ptn.matcher(mdText)
        if (!matcher.find()) {
            return mdText
        }
        val imgPath = matcher.group(2)
        imgPath?.let {
            if (isUrlPrefix(imgPath) || !isPathExCheck(imgPath)) {
                return mdText
            }
            val baseType = imgEx2BaseType(imgPath)
            if ("" == baseType) {
                return mdText
            }
            val file = File(imgPath)

            val bytes = ByteArray(file.length().toInt())
            try {
                val buf = BufferedInputStream(FileInputStream(file))
                buf.read(bytes, 0, bytes.size)
                buf.close()
            } catch (e: FileNotFoundException) {
                e.message?.let { Log.d(tag, it) }
            } catch (e: IOException) {
                e.message?.let { Log.d(tag, it) }
            }
            val base64Img = baseType + Base64.encodeToString(bytes, Base64.NO_WRAP)
            return mdText.replace(imgPath, base64Img)
        }
        return ""
    }

    private fun isUrlPrefix(text: String): Boolean {
        return text.startsWith("http://") || text.startsWith("https://")
    }

    private fun isPathExCheck(text: String): Boolean {
        return (text.endsWith(".png")
                || text.endsWith(".jpg")
                || text.endsWith(".jpeg")
                || text.endsWith(".gif"))
    }

    private fun imgEx2BaseType(text: String): String {
        return if (text.endsWith(".png")) {
            "data:image/png;base64,"
        } else if (text.endsWith(".jpg") || text.endsWith(".jpeg")) {
            "data:image/jpg;base64,"
        } else if (text.endsWith(".gif")) {
            "data:image/gif;base64,"
        } else {
            ""
        }
    }
}
