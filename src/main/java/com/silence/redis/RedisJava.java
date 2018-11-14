package com.silence.redis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import redis.clients.jedis.Jedis;

public class RedisJava {
	public static void main(String[] args) {
		// 连接Redis
		Jedis jedis = new Jedis("192.168.11.227");
		// 读取key字符串
		String keyStr = readTxt("D:\\upload\\txt.txt");
		// 打印key字符串
		System.out.println("返回的内容是：" + keyStr);

		if (keyStr != null && keyStr != "") {
			// 将key字符串转换为数组
			String[] strArryList = keyStr.split(",");

			if (strArryList != null && strArryList.length > 0) {
				for (int i = 0; i < strArryList.length; i++) {
					// 得到该key下的所有键值
					Map<String, String> map = jedis.hgetAll(strArryList[i]);
					Info info = JsonUtils.jsonToPojo(map.get("Info"), Info.class);
					// 修改数据
					info.setSfypz("0");
					map.remove("Info");
					map.put("Info", JsonUtils.objectToJson(info));
					// 更新数据
					jedis.hmset(strArryList[i], map);
				}
			}
		}

	}

	public static String readTxt(String filePath) {
		String lineTxt = null;
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
				BufferedReader br = new BufferedReader(isr);

				lineTxt = br.readLine();
				System.out.println("读取的内容信息：" + lineTxt + "\n");
				br.close();
				return lineTxt;
			} else {
				lineTxt = "文件不存在!";
				return lineTxt;
			}
		} catch (Exception e) {
			lineTxt = "文件读取错误!";
			return lineTxt;
		}

	}
}