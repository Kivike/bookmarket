package fi.oulu.bookmarket2020.model

import androidx.room.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "phone") val phone: Int = 0,
    @ColumnInfo(name = "password") val password: String
) {

}

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User): Long

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    fun getUser(email: String): User?

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>
}