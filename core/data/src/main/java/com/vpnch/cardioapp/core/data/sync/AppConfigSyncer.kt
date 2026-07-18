package com.vpnch.cardioapp.core.data.sync

import com.vpnch.cardioapp.core.database.dao.HelpDao
import com.vpnch.cardioapp.core.database.dao.SurveyDao
import com.vpnch.cardioapp.core.database.entity.help.HelpContactEntity
import com.vpnch.cardioapp.core.database.entity.survey.SurveyLinkEntity
import java.net.URL
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val CONFIG_URL =
    "https://gist.githubusercontent.com/Vadim-Ponochevny/716e45c21768f320764c484b109e52e0/raw/survey_config.json"

class AppConfigSyncer @Inject constructor(
    private val surveyDao: SurveyDao,
    private val helpDao: HelpDao,
) {
    suspend fun sync() {
        val json = fetchJson() ?: return
        syncSurvey(json)
        syncContacts(json)
    }

    private suspend fun fetchJson(): JSONObject? = withContext(Dispatchers.IO) {
        try {
            JSONObject(URL(CONFIG_URL).readText())
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun syncSurvey(json: JSONObject) {
        val url = json.optString("survey_url")
        val id = json.optString("survey_id")
        if (url.isBlank() || id.isBlank()) return
        surveyDao.upsertSurveyLink(
            SurveyLinkEntity(
                id = id,
                title = "Опрос",
                url = url,
                isActive = true,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
            )
        )
    }

    private suspend fun syncContacts(json: JSONObject) {
        val array = json.optJSONArray("contacts") ?: return
        val entities = (0 until array.length()).mapNotNull { i ->
            val obj = array.optJSONObject(i) ?: return@mapNotNull null
            val id = obj.optString("id").takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val title = obj.optString("title").takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val phone = obj.optString("phone").takeIf { it.isNotBlank() } ?: return@mapNotNull null
            HelpContactEntity(
                id = id,
                title = title,
                phone = phone,
                description = obj.optString("description").takeIf { it.isNotBlank() },
                sortOrder = obj.optInt("sort_order", 0),
                isActive = true,
            )
        }
        if (entities.isNotEmpty()) {
            helpDao.upsertHelpContacts(entities)
        }
    }
}
