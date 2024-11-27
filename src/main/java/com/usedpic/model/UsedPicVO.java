package com.usedpic.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.used.model.UsedVO;

@Entity
@Table(name = "usedpic") // 商品照片資料表
public class UsedPicVO implements Serializable  {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
    @Column(name = "usedPicNo") // 二手商品照片編號
    private Integer usedPicNo;

    @ManyToOne(fetch = FetchType.LAZY) // 多張照片對應同一個二手商品
    @JoinColumn(name = "usedNo", referencedColumnName = "usedNo", nullable = false) // 外鍵關聯到 Used 表
    private UsedVO usedVO;

    @Lob // 表示二進位大資料
    @Column(name = "usedPics", nullable = true,columnDefinition = "longblob") // 二手商品照片欄位
    private byte[] usedPics;
    
    @Column(name = "usedPicName", nullable = true, length = 30) //二手商品照片檔名
    @Pattern(regexp = "^[\\u4e00-\\u9fffA-Za-z0-9\\s\\p{P}\\p{S}\\u3100-\\u312F\\u31A0-\\u31BF\\u3040-\\u309F\\u30A0-\\u30FF]{1,30}$", message = "圖片名稱: 只能是中、英、日文字母、數字和特殊符號 , 且長度最多30個字")
    private String usedPicName;


	public UsedPicVO() {
        super();
    }

    public UsedPicVO(Integer usedPicNo, UsedVO usedVO, byte[] usedPics,String usedPicName) {
        this.usedPicNo = usedPicNo;
        this.usedVO = usedVO;
        this.usedPics = usedPics;
        this.usedPicName = usedPicName;
    }

    public String getUsedPicName() {
    	return usedPicName;
    }
    
    public void setUsedPicName(String usedPicName) {
    	this.usedPicName = usedPicName;
    }
    
    public Integer getUsedPicNo() {
        return usedPicNo;
    }

    public void setUsedPicNo(Integer usedPicNo) {
        this.usedPicNo = usedPicNo;
    }

    public UsedVO getUsedVO() {
        return usedVO;
    }

    public void setUsedVO(UsedVO usedVO) {
        this.usedVO = usedVO;
    }

    public byte[] getUsedPics() {
        return usedPics;
    }

    public void setUsedPics(byte[] usedPics) {
        this.usedPics = usedPics;
    }
}
