package com.vpnch.cardioapp.navigation

import androidx.annotation.DrawableRes
import com.vpnch.cardioapp.R

object CardioDestinations {
    const val TODAY_GRAPH = "today_graph"
    const val TODAY_HOME = "today_home"
    const val HEALTH_RECORD_CREATE = "health_record_create?recordId={recordId}"
    const val HEALTH_RECORD_CREATE_ARG = "recordId"
    const val HEALTH_RECORDS_LIST = "health_records_list?date={date}"
    const val HEALTH_RECORDS_DATE_ARG = "date"
    const val HEALTH_RECORD_DETAIL = "health_record_detail/{recordId}"
    const val HEALTH_RECORD_DETAIL_ARG = "recordId"
    const val HEALTH_METRIC_EDIT = "health_metric_edit/{recordId}/{metricType}"
    const val HEALTH_METRIC_EDIT_RECORD_ARG = "recordId"
    const val HEALTH_METRIC_EDIT_TYPE_ARG = "metricType"
    const val VITAMINS = "vitamins"
    const val HISTORY = "history"
    const val HELP = "help"
//    const val SURVEY = "survey?url={url}"
//    const val SURVEY_URL_ARG = "url"
//
//    fun survey(url: String): String = "survey?url=${android.net.Uri.encode(url)}"

    fun healthRecordCreate(recordId: String? = null): String {
        return if (recordId == null) {
            "health_record_create"
        } else {
            "health_record_create?recordId=$recordId"
        }
    }

    fun healthRecordsList(date: String? = null): String {
        return if (date == null) {
            "health_records_list"
        } else {
            "health_records_list?date=$date"
        }
    }

    fun healthRecordDetail(recordId: String): String = "health_record_detail/$recordId"

    fun healthMetricEdit(recordId: String, metricType: String): String {
        return "health_metric_edit/$recordId/$metricType"
    }
}

enum class TopLevelDestination(
    val route: String,
    val label: String,
    @DrawableRes val iconRes: Int
) {
    TODAY(CardioDestinations.TODAY_GRAPH, "Сегодня", R.drawable.ic_home),
    HISTORY(CardioDestinations.HISTORY, "История", R.drawable.ic_history),
    HELP(CardioDestinations.HELP, "Помощь", R.drawable.ic_help)
}
