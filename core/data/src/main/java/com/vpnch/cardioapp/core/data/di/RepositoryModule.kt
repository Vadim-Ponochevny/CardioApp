package com.vpnch.cardioapp.core.data.di

import com.vpnch.cardioapp.core.data.repository.HealthRecordRepositoryImpl
import com.vpnch.cardioapp.core.data.repository.HelpRepositoryImpl
import com.vpnch.cardioapp.core.data.repository.PatientRepositoryImpl
import com.vpnch.cardioapp.core.data.repository.SurveyRepositoryImpl
import com.vpnch.cardioapp.core.data.repository.VitaminRepositoryImpl
import com.vpnch.cardioapp.core.domain.repository.HealthRecordRepository
import com.vpnch.cardioapp.core.domain.repository.HelpRepository
import com.vpnch.cardioapp.core.domain.repository.PatientRepository
import com.vpnch.cardioapp.core.domain.repository.SurveyRepository
import com.vpnch.cardioapp.core.domain.repository.VitaminRepository
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
