package com.liu.learnjava.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

//这个标注表示它用于继承
//@MappedSuperclass
public class AbstractEntity {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(nullable = false, updatable = false)
	public Long getId() {
		return id;
	}

//	@Column(nullable = false, updatable = false)
	public Long getCreatedAt() {
		return createdAt;
	}
	//		getCreatedDateTime 是由计算机得出的属性，不是数据库的值，因此必须标注transient
//	@Transient//短暂的
	public ZonedDateTime getCreatedDateTime() {
		return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
	}
//prepersist注释表示再JavaBean持久化到数据库之前，hibernate会先执行该方法设置好createAt的属性
//	@PrePersist
//	public void preInsert() {
//		setCreatedAt(System.currentTimeMillis());
//	}


	public void setId(Long id) {
		this.id = id;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	private Long id;
	private Long createdAt;

}
