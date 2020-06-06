package hm.moe.pokkedoll.takuya

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack

/**
 * @version 1.0
 */
object Serialization {
  def i2s(items: Array[ItemStack]): String = {
    if(items==null || items.length != 54) " " else {
      val yaml = new YamlConfiguration
      (0 to 53).foreach(i => {
        yaml.set(i.toString, items(i))
      })
      yaml.saveToString()
    }
  }

  def s2i(string: String): Option[Array[ItemStack]] = {
    val items = new Array[ItemStack](54)
    if(string==null || string=="" || string==" ") None else {
      val yaml = new YamlConfiguration
      try {
        yaml.loadFromString(string)
        (0 to 53).foreach(i => {
          items(i) = yaml.getItemStack(i.toString, new ItemStack(Material.AIR))
        })
        Some(items)
      } catch {
        case e: Exception => e.printStackTrace(); None
      }
    }
  }
}
