package com.luowei.qukanhelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.luowei.logwherelibrary.logDebug
import java.util.*

class QuKanHelper(private val service: AccessibilityService) {
    private val windowMap = HashMap<String, (service: AccessibilityService, event: AccessibilityEvent) -> Unit>()
    private var currentPage: IPage? = null
    private val pages = arrayListOf(MainPage(service), DetailPage(service), DialogPage(service))
    private val errorPage = ErrorPage(service)


    fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName != "com.jifen.qukan") {
//            currentPage?.leave(service)
            return
        }
        val eventType = event.eventType
        val accessibilityNodeInfo = service.rootInActiveWindow
        val className = event.className
        logDebug(
            " eventType: " + AccessibilityEvent.eventTypeToString(eventType) +
                    " getText :" + event.text.toString()
                    + "  getClassName:" + className
        )

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                windowChange(service, event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                currentPage?.handleAccessibilityEvent(event)
            }
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }
        }
//        AccessibilityServiceUtils.scrollVertical(service)

//        accessibilityNodeInfo ?: return
//        function?.invoke(service, event)
//        LayoutInspector.printPacketInfo(service.rootInActiveWindow)
//        if (event.className == "android.support.v7.widget.RecyclerView") {
//        }
//            rootInActiveWindow.getChild(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
//        if (accessibilityNodeInfo != null) {
//            printPacketInfo(accessibilityNodeInfo)
//        }

    }

    private var currentWindow: String = ""

    private fun windowChange(
        service: AccessibilityService,
        event: AccessibilityEvent
    ) {
        val className = event.className.toString()
        val page = pages.find { it.matchPage(className) } ?: errorPage
        if (currentPage != page) {
            currentPage?.leave()
            page?.enter()
            currentPage = page
        }
        currentPage?.handleAccessibilityEvent(event)
    }
}