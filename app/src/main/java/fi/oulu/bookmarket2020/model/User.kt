package fi.oulu.bookmarket2020.model

import androidx.room.*
import org.intellij.lang.annotations.JdkConstants

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = -1,
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

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUser(email: String): User?
}