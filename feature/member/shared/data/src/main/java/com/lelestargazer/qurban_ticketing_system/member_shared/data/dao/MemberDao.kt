package com.lelestargazer.qurban_ticketing_system.member_shared.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMember(member: MemberEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Query(
        """
            SELECT COUNT(*) FROM member_table
        """
    )
    fun getActiveMemberCount(): Flow<Int>

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Query("SELECT * FROM member_table WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun selectAllMembers(query: String): PagingSource<Int, MemberEntity>

    @Query("SELECT * FROM member_table WHERE (name LIKE '%' || :query || '%' AND status = :status) ORDER BY name ASC")
    fun selectMembersByStatus(query: String, status: QurbanStatus): PagingSource<Int, MemberEntity>

    @Query("SELECT * FROM member_table WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun getActiveMembers(query: String): PagingSource<Int, MemberEntity>

    @Query("SELECT * FROM member_table WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun getActiveMembersAsList(query: String): List<MemberEntity>

    @Query("SELECT * FROM member_table WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun getInactiveMembers(query: String): PagingSource<Int, MemberEntity>

    @Query("SELECT * FROM member_table")
    fun getActiveParticipant(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM member_table")
    fun getInactiveParticipant(): Flow<List<MemberEntity>>

    @Delete
    suspend fun deleteMember(member: MemberEntity)
}