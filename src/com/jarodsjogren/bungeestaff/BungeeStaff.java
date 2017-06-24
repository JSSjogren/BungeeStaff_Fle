package com.jarodsjogren.bungeestaff;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class BungeeStaff extends JavaPlugin implements Listener{

	String prefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix"));
	String report = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("report"));
	String helpop = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("helpop"));
	
	@Override
	public void onEnable()
	{
		Bukkit.getPluginManager().getPlugin("BungeeStaff").saveDefaultConfig(); //Saving the config
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getLogger().info("Created by TheBeyonder");
		getLogger().info("BungeeStaff enabled");
	}

	@Override
	public void onDisable()
	{
		getLogger().info("Created by TheBeyonder");
		getLogger().info("BungeeStaff enabled");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player msgp = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("report") && args.length >= 2) {
			String reportee = Bukkit.getServer().getPlayer(args[0]).getName();
			report = placeholderReport(report, sender.getName(), reportee);
			String msg = "";
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			for (int i = 1; i < args.length; i++)
			{
				msg += args[i] + " ";
			}
			try {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.hasPermission("bungeestaff.isstaff")) {
						out.writeUTF("Message");
						out.writeUTF(p.getName());
						out.writeUTF(prefix + report +  msg);
						msgp.sendPluginMessage(this, "BungeeCord", b.toByteArray());
					}
				}
			} catch (Exception e)
			{}
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("helpop") && args.length >= 1)
		{
			helpop = placeholderHelpop(helpop, sender.getName());
			String msg = "";
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			for (int i = 0; i < args.length; i++)
			{
				msg += args[i] + " ";
			}
			try {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.hasPermission("bungeestaff.isstaff")) {
						out.writeUTF("Message");
						out.writeUTF(p.getName());
						out.writeUTF(prefix + helpop + msg);
						msgp.sendPluginMessage(this, "BungeeCord", b.toByteArray());
					}
				}
			} catch (Exception e)
			{}
			return true;
		}
		
		else if (cmd.getName().equalsIgnoreCase("bungeestaff") || cmd.getName().equalsIgnoreCase("bs") && args[0].equalsIgnoreCase("reload")
				&& args.length == 1)
		{
			Bukkit.getPluginManager().getPlugin("BungeeStaff").reloadConfig();
			prefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix"));
			report = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("report"));
			helpop = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("helpop"));
			sender.sendMessage(prefix + ChatColor.RED + "Config has been reloaded.");
			return true;
		}

		return false;
	}


	@EventHandler
	public void staffJoin (PlayerJoinEvent e)
	{

		if (e.getPlayer().hasPermission("bungeestaff.isstaff"))
		{
			
			for (Player p : Bukkit.getServer().getOnlinePlayers())
			{
				if (p.hasPermission("bungeestaff.isstaff") && !p.getName().equals(e.getPlayer().getName()))
				{
					ByteArrayOutputStream b = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(b);
					e.getPlayer().sendMessage(p.getName());
					try {
						out.writeUTF("Message");
						out.writeUTF(p.getName());
						out.writeUTF(prefix + ChatColor.GREEN + e.getPlayer().getName() + ChatColor.RED + " has joined the network.");
						p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
					} catch (Exception exc)
					{}
				}
			}
		}
	}
	
	public String placeholderHelpop(String message, String sender) {
		if (message.contains("{sender}"))
		{
			message = message.replaceAll("\\{sender\\}", sender);
		}
		return message;
	}
	
	public String placeholderReport(String message, String sender, String offender)
	{
		if (message.contains("{sender}"))
		{
			message = message.replaceAll("\\{sender\\}", sender);
		}
		
		if (message.contains("{offender}"))
		{
			message = message.replaceAll("\\{offender\\}", offender);
		}
		
		return message;
	}
}
