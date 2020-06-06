package hm.moe.pokkedoll.takuya

import java.sql.SQLException
import java.util.UUID

import com.zaxxer.hikari.HikariDataSource
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class TakuyaAPI(val plugin: Plugin, val hikari: HikariDataSource) {
  private val TABLE = "ib"
  private val VERSION: Int = 1

  //TODO 初期化
  /*
  try {
    val c = hikari.getConnection
    c.prepareStatement("")
  } catch {
    case e: SQLException =>
      e.printStackTrace()
  }
  */

  def hasUUID(uuid: String): Boolean = {
    val c = hikari.getConnection
    val ps = c.prepareStatement(s"SELECT `uuid` FROM $TABLE WHERE `uuid`=UUID_TO_BIN(?)")
    try {
      ps.setString(1, uuid.toString)
      val rs = ps.executeQuery()
      rs.next()
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        false
    } finally {
      ps.close()
      c.close()
    }
  }

  def insert(uuid: String): Unit = {
    val c = hikari.getConnection
    val ps = c.prepareStatement(s"INSERT INTO $TABLE (uuid, version) VALUES(UUID_TO_BIN(?), ?)")
    try {
      ps.setString(1, uuid.toString)
      ps.setInt(2, VERSION)
      ps.executeUpdate()
    } catch {
      case e: SQLException =>
        e.printStackTrace()
    } finally {
      ps.close()
      c.close()
    }
  }

  def getVersion(uuid: UUID): Int = {
    val c = hikari.getConnection
    val ps = c.prepareStatement(s"SELECT `version` FROM $TABLE WHERE `uuid`=UUID_TO_BIN(?)")
    try {
      ps.setString(1, uuid.toString)
      val rs = ps.executeQuery()
      if(rs.next()) Option(rs.getInt("version")).getOrElse(0) else 0
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        -1
    } finally {
      ps.close()
      c.close()
    }
  }

  def isOld(uuid: UUID): Boolean = {
    VERSION < getVersion(uuid)
  }

  def getInventory(uuid: UUID): Option[Array[ItemStack]] = {
    val c = hikari.getConnection
    val ps = c.prepareStatement(s"SELECT `inventory` FROM $TABLE WHERE `uuid`=UUID_TO_BIN(?)")
    try {
      ps.setString(1, uuid.toString)
      val rs = ps.executeQuery()
      if(rs.next()) Serialization.s2i(rs.getString("inventory")) else None
    } catch {
      case e: SQLException =>
        e.printStackTrace()
        None
    } finally {
      ps.close()
      c.close()
    }
  }

  def setInventory(uuid: UUID, items: Array[ItemStack]): Unit = {
    val c = hikari.getConnection()
    val ps = c.prepareStatement(s"UPDATE $TABLE SET `inventory`=? WHERE `uuid`=UUID_TO_BIN(?)")
    try {
      val a = Serialization.i2s(items)
      ps.setString(1, a)
      ps.setString(2, uuid.toString)
      ps.executeUpdate()
    } catch {
      case e: SQLException =>
        e.printStackTrace()
    } finally {
      ps.close()
      c.close()
    }
  }

  def close(): Unit = if(hikari!=null && !hikari.isClosed) hikari.close()
}
