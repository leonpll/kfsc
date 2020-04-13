package com.pactera;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * @Desc: kafka消息传输对象
 * @Author: wanliang(wanliang@mapbar.com)
 * @Date: Created in 2017-8-23
 * @copyright Navi WeCloud
 */
public class KafkaMessage implements Serializable {

	// 消息key
	private String key;
	// 消息topic
	private String topic;
	// 消息内容
	private byte[] message;
	// 消息发送时间
	private Date sendTime;
	// 消息业务Id
	private String serviceId;
	private String traceId;
	// 消息版本号
	private String version;

	private String span;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	@Override
	public String toString() {
		return "KafkaMessage{" + "key='" + key + '\'' + ", topic='" + topic + '\'' + ", message="
				+ Arrays.toString(message) + ", sendTime=" + sendTime + ", serviceId='" + serviceId + '\''
				+ ", traceId='" + traceId + '\'' + ", version='" + version + '\'' + '}';
	}

	public String getSpan() {
		return span;
	}

	public void setSpan(String span) {
		this.span = span;
	}
}
