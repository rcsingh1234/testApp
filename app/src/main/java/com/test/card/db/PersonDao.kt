package com.test.card.db

import androidx.room.*

@Dao
interface PersonDao {
    @get:Query("SELECT * FROM Entity")
    val entityList: List<Entity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: Entity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg entity: Entity)
}