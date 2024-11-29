package com.ShoppingCart.model;

//BOOK 類別實作了 java.io.Serializable，表示該類別的物件可以被序列化
public class Boutique implements java.io.Serializable {
	private static final long serialVersionUID = 1L; // 序列化版本號，確保序列化與反序列化的版本一致性

	// 無參數建構子，初始化所有屬性為預設值
	public Boutique() {
		name = ""; // 書名初始化為空字串
		author = ""; // 作者初始化為空字串
		publisher = ""; // 出版社初始化為空字串
		price = 0.0; // 價格初始化為 0.0
		quantity = 0; // 數量初始化為 0
	}

	// 私有屬性，對應書籍的相關資訊
	private String name; // 書名
	private String author; // 作者
	private String publisher; // 出版社
	private Double price; // 價格
	private Integer quantity; // 數量

	// Getter 和 Setter 方法
	// 用於存取和修改書名
	public String getName() {
		return name; // 返回書名
	}
	public void setName(String name) {
		this.name = name; // 設定書名
	}

	// 用於存取和修改作者
	public String getAuthor() {
		return author; // 返回作者
	}
	public void setAuthor(String author) {
		this.author = author; // 設定作者
	}

	// 用於存取和修改出版社
	public String getPublisher() {
		return publisher; // 返回出版社
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher; // 設定出版社
	}

	// 用於存取和修改價格
	public Double getPrice() {
		return price; // 返回價格
	}
	public void setPrice(Double price) {
		this.price = price; // 設定價格
	}

	// 用於存取和修改數量
	public Integer getQuantity() {
		return quantity; // 返回數量
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity; // 設定數量
	}

	// 重新實作 toString 方法，用於返回該物件的文字描述
	@Override
	public String toString() {
		return "BOOK [name=" + name + ", author=" + author + ", publisher=" + publisher + ", price=" + price
				+ ", quantity=" + quantity + "]";
		// 返回書名、作者、出版社、價格、數量的描述
	}

	// 重新實作 hashCode 方法，用於生成物件的哈希碼
	@Override
	public int hashCode() {
		final int prime = 31; // 定義一個質數，用於計算哈希碼
		int result = 1; // 初始值為 1
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		// 以書名為唯一標識，計算哈希碼，若書名為 null，則設為 0
		return result;
	}

	// 重新實作 equals 方法，用於比較兩個物件是否相等
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // 若兩個物件引用相同，則相等
			return true;
		if (obj == null) // 若比較的物件為 null，則不相等
			return false;
		if (getClass() != obj.getClass()) // 若類別不同，則不相等
			return false;
		Boutique other = (Boutique) obj; // 將比較的物件轉型為 BOOK
		if (name == null) { // 若當前物件的書名為 null
			if (other.name != null) // 若另一個物件的書名不為 null，則不相等
				return false;
		} else if (!name.equals(other.name)) // 比較書名是否相等
			return false;
		return true; // 若以上條件都不成立，則物件相等
	}
}
