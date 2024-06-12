package com.example.firebaseauthtest.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkDao {
    @Query("SELECT * FROM marks")
    fun getAllMarks(): Flow<List<Mark>>

    @Query("SELECT * FROM marks WHERE groupNumber = :groupNumber")
    fun getMarksForGroup(groupNumber: String): List<Mark>

    @Query("SELECT * FROM marks WHERE studentNumber = :studentNumber")
    fun getMarksForStudent(studentNumber: Int): List<Mark>

    @Query("SELECT * FROM marks WHERE groupNumber = :groupNumber AND studentNumber = :studentNumber AND data = :date")
    fun getMarksForStudentInGroupByDate(groupNumber: String, studentNumber: Int, date: String): List<Mark>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mark: Mark)

    @Upsert
    fun upsert(mark: Mark)

    @Update
    fun update(mark: Mark)

    @Delete
    fun delete(mark: Mark)
}

@Entity(tableName = "marks")
data class Mark(
    val data: String? = null,
    val mark: String? = null,
    val disciplineName: String? = null,
    val groupNumber: String? = null,
    val studentNumber: Int? = null,
    val theme: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}