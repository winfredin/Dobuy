package com.followers.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Followers") // 對應資料庫中的 FOLLOWERS 表
public class FollowersVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer trackListNo;  // 櫃位追蹤清單編號 (主鍵)
    private Integer followersNo;  // 會員編號 (外鍵)
    private Integer counterNo;    // 櫃位編號 (外鍵)

    public FollowersVO() {  // 必需的無參數建構子
    }

    @Id
    @Column(name = "trackListNo", nullable = false)  // 對應資料庫中的 TRACKLIST_NO 欄位
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自動遞增策略
    public Integer getTrackListNo() {
        return trackListNo;
    }

    public void setTrackListNo(Integer trackListNo) {
        this.trackListNo = trackListNo;
    }

    @Column(name = "followersNo", nullable = false)  // 對應資料庫中的 FOLLOWERS_NO 欄位
    @NotNull(message = "會員編號: 請勿空白")
    public Integer getFollowersNo() {
        return followersNo;
    }

    public void setFollowersNo(Integer followersNo) {
        this.followersNo = followersNo;
    }

    @Column(name = "counterNo", nullable = false)  // 對應資料庫中的 COUNTER_NO 欄位
    @NotNull(message = "櫃位編號: 請勿空白")
    public Integer getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(Integer counterNo) {
        this.counterNo = counterNo;
    }
}
