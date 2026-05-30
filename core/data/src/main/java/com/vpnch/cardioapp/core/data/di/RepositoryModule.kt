package com.vpnch.cardioapp.core.data.di

import com.vpnch.cardioapp.core.data.HealthRecordRepositoryImpl
import com.vpnch.cardioapp.core.data.HelpRepositoryImpl
import com.vpnch.cardioapp.core.data.PatientRepositoryImpl
import com.vpnch.cardioapp.core.data.SurveyRepositoryImpl
import com.vpnch.cardioapp.core.data.VitaminRepositoryImpl
import com.vpnch.cardioapp.core.domain.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.HelpRepository
import com.vpnch.cardioapp.core.domain.PatientRepository
import com.vpnch.cardioapp.core.domain.SurveyRepository
import com.vpnch.cardioapp.core.domain.VitaminRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindPatientRepository(repository: PatientRepositoryImpl): PatientRepository

    @Binds
    fun bindHealthRecordRepository(repository: HealthRecordRepositoryImpl): HealthRecordRepository

    @Binds
    fun bindVitaminRepository(repository: VitaminRepositoryImpl): VitaminRepository

    @Binds
    fun bindSurveyRepository(repository: SurveyRepositoryImpl): SurveyRepository

    @Binds
    fun bindHelpRepository(repository: HelpRepositoryImpl): HelpRepository
}
