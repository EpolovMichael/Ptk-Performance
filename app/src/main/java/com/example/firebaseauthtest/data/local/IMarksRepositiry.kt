package com.example.firebaseauthtest.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarksRepository @Inject constructor(private val markDao: MarkDao) {

    val allMarks: Flow<List<Mark>> = markDao.getAllMarks()

    suspend fun getMarksForGroup(groupNumber: String) {
        markDao.getMarksForGroup(groupNumber)
    }

    suspend fun getMarksForStudent(studentId: Int) {
        markDao.getMarksForStudent(studentId)
    }

    suspend fun getMarksForStudentInGroupByDate(groupNumber: String, studentId: Int, date: String) {
        markDao.getMarksForStudentInGroupByDate(groupNumber, studentId, date)
    }

    suspend fun insert(mark: Mark) {
        markDao.insert(mark)
    }

    suspend fun upsert(mark: Mark){
        markDao.upsert(mark)
    }

    suspend fun update(mark: Mark) {
        markDao.update(mark)
    }

    suspend fun delete(mark: Mark) {
        markDao.delete(mark)
    }
}