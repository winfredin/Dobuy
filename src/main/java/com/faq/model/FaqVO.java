package com.faq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "faq")
public class FaqVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    @Column(name = "faqNo", nullable = false)
    private Integer faqNo;

    @Column(name = "ques", nullable = false, length = 500)
    private String ques;

    @Column(name = "ans", nullable = false, length = 500)
    private String ans;

    @Column(name = "counterNo", nullable = false)
    private Integer counterNo;

    // Getter 和 Setter 方法
    public Integer getFaqNo() {
        return faqNo;
    }

    public void setFaqNo(Integer faqNo) {
        this.faqNo = faqNo;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public Integer getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(Integer counterNo) {
        this.counterNo = counterNo;
    }
    
}
