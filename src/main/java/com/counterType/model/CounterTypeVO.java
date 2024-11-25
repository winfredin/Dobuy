package com.counterType.model;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.counter.model.CounterVO;




@Entity
@Table(name = "countertype")
public class CounterTypeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTERTYPENO")
    private Integer counterTypeNo;

    @Column(name = "COUNTERTNAME", nullable = false, length = 100)
    private String counterTName;
    
    
    @OneToMany(mappedBy = "counterTypeVO", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CounterVO> counters;

    public CounterTypeVO() {
    }

    public Integer getCounterTypeNo() {
        return counterTypeNo;
    }

    public void setCounterTypeNo(Integer counterTypeNo) {
        this.counterTypeNo = counterTypeNo;
    }

    public String getCounterTName() {
        return counterTName;
    }

    public void setCounterTName(String counterTName) {
        this.counterTName = counterTName;
    }
    
//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="counterTypeVO")
//	@OrderBy("COUNTERNO asc")
//	//註1:【現在是設定成 cascade="all" lazy="false" inverse="true"之意】
//	//註2:【mappedBy="多方的關聯屬性名"：用在雙向關聯中，把關係的控制權反轉】【deptVO是EmpVO的屬性】
//	//註3:【原預設為@OneToMany(fetch=FetchType.LAZY, mappedBy="deptVO")之意】--> 【是指原為  lazy="true"  inverse="true"之意】
//	//FetchType.EAGER : Defines that data must be eagerly fetched
//	//FetchType.LAZY  : Defines that data can be lazily fetched
//	public Set<CounterVO> getCounters() {
//		return this.counters;
//	}
//
//	public void setCounters(Set<CounterVO> counters) {
//		this.counters = counters;
//	}
    public Set<CounterVO> getCounters() {
        return counters;
    }

    public void setCounters(Set<CounterVO> counters) {
        this.counters = counters;
    }
}
