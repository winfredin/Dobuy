package com.msg.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "counterInform")
public class MsgVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counterInformNo")
    private Integer counterInformNo;

    @NotNull
    @NotEmpty(message = "訊息內文請勿空白。")
    @Size(max = 1000)
    @Column(name = "informMsg")
    private String informMsg;

    @NotNull(message = "發佈日期請勿空白。")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "informDate") // 移除 insertable 和 updatable 限制
    private Timestamp informDate;


    public Integer getCounterInformNo() {
        return counterInformNo;
    }

    public void setCounterInformNo(Integer counterInformNo) {
        this.counterInformNo = counterInformNo;
    }

    public String getInformMsg() {
        return informMsg;
    }

    public void setInformMsg(String informMsg) {
        this.informMsg = informMsg;
    }

    public Timestamp getInformDate() {
        return informDate;
    }

    public void setInformDate(Timestamp informDate) {
        this.informDate = informDate;
    }
}

