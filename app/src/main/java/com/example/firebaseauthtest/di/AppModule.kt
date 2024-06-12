package com.example.firebaseauthtest.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.firebaseauthtest.data.AuthRepository
import com.example.firebaseauthtest.data.AuthRepositoryImpl
import com.example.firebaseauthtest.data.firebase.RealtimeFirebaseDB
import com.example.firebaseauthtest.data.local.MarksRepository
import com.example.firebaseauthtest.data.local.MarkDao
import com.example.firebaseauthtest.data.local.MarksDatabase
import com.example.firebaseauthtest.data.repository.RealtimeFirebaseRepository
import com.example.firebaseauthtest.data.repository.RealtimeFirebaseRepositoryImpl
import com.example.firebaseauthtest.presentation.navigation_container_screen.NavigationContainerViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFireStoreDatabaseReference() = Firebase.firestore

    @Provides
    @Singleton
    fun providesFirebaseDatabaseReference() = Firebase.database.reference

    @Provides
    @Singleton
    fun provideRealtimeDBClass(
        userReference: DatabaseReference,
        repository: AuthRepository
    ): RealtimeFirebaseDB =
        RealtimeFirebaseDB(userReference = userReference, repository = repository)

    @Provides
    @Singleton
    fun provideRepository(
        firebase: RealtimeFirebaseDB
    ): RealtimeFirebaseRepository = RealtimeFirebaseRepositoryImpl(firebase)

    @Provides
    fun provideMarksDatabase(@ApplicationContext context: Context): MarksDatabase =
        Room.databaseBuilder(
            context,
            MarksDatabase::class.java,
            "marks_database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideItemDao(marksDatabase: MarksDatabase): MarkDao = marksDatabase.markDao()

    @Provides
    fun provideItemRepository(marksDao: MarkDao): MarksRepository = MarksRepository(marksDao)

    @Provides
    @Singleton
    fun providesRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        dataBase: DatabaseReference
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, dataBase)
    }

    @Provides
    @Singleton
    fun provideNavigationViewModel(navigationViewModel: NavigationContainerViewModel): ViewModel {
        return navigationViewModel
    }
}