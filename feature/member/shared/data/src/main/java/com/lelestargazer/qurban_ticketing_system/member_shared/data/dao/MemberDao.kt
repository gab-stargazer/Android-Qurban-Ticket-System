package com.lelestargazer.qurban_ticketing_system.member_shared.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMember(member: MemberEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Query(
        """
            SELECT COUNT(*) FROM member_table WHERE is_active == 1
        """
    )
    fun getActiveMemberCount(): Flow<Int>

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Query("SELECT * FROM member_table WHERE name LIKE '%' || :query || '%' AND is_active == 1 ORDER BY name ASC")
    fun getActiveMembers(query: String): PagingSource<Int, MemberEntity>

    @Query("SELECT * FROM member_table WHERE name LIKE '%' || :query || '%' AND is_active == 1 ORDER BY name ASC")
    suspend fun getActiveMembersAsList(query: String): List<MemberEntity>

    @Query("SELECT * FROM member_table WHERE name LIKE '%' || :query || '%' AND is_active == 0 ORDER BY name ASC")
    fun getInactiveMembers(query: String): PagingSource<Int, MemberEntity>

    @Query("SELECT * FROM member_table WHERE is_active = 1")
    fun getActiveParticipant(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM member_table WHERE is_active = 0")
    fun getInactiveParticipant(): Flow<List<MemberEntity>>
}