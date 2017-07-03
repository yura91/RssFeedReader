package com.snijsure.rssreader.model;


import java.util.List;

public class WebSite {
	Integer _id;
	String _title;
	String _link;
	String _description;
	String _rss_link;
	String itemList;

	public String getRSSLink() {
		return _rss_link;
	}

	public void setRSSLink(String _rss_link) {
		this._rss_link = _rss_link;
	}

	// constructor
	public WebSite(){
		
	}

	public String getItemList() {
		return itemList;
	}

	public void setItemList(String itemList) {
		this.itemList = itemList;
	}

	// constructor with parameters
	public WebSite(String title, String link, String rss_link, String description, String itemList){
		this._title = title;
		this._link = link;
		this._rss_link = rss_link;
		this._description = description;
		this.itemList = itemList;
	}
	
	/**
	 * All set methods
	 * */
	public void setId(Integer id){
		this._id = id;
	}
	
	public void setTitle(String title){
		this._title = title;
	}
	
	public void setLink(String link){
		this._link = link;
	}
	
	public void setDescription(String description){
		this._description = description;
	}
	
	/**
	 * All get methods
	 * */
	public Integer getId(){
		return this._id;
	}
	
	public String getTitle(){
		return this._title;
	}
	
	public String getLink(){
		return this._link;
	}
	
	public String getDescription(){
		return this._description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WebSite webSite = (WebSite) o;

		if (_id != null ? !_id.equals(webSite._id) : webSite._id != null) return false;
		if (_title != null ? !_title.equals(webSite._title) : webSite._title != null) return false;
		if (_link != null ? !_link.equals(webSite._link) : webSite._link != null) return false;
		if (_description != null ? !_description.equals(webSite._description) : webSite._description != null)
			return false;
		if (_rss_link != null ? !_rss_link.equals(webSite._rss_link) : webSite._rss_link != null)
			return false;
		return itemList != null ? itemList.equals(webSite.itemList) : webSite.itemList == null;

	}



	@Override
	public int hashCode() {
		int result = _id != null ? _id.hashCode() : 0;
		result = 31 * result + (_title != null ? _title.hashCode() : 0);
		result = 31 * result + (_link != null ? _link.hashCode() : 0);
		result = 31 * result + (_description != null ? _description.hashCode() : 0);
		result = 31 * result + (_rss_link != null ? _rss_link.hashCode() : 0);
		result = 31 * result + (itemList != null ? itemList.hashCode() : 0);
		return result;
	}
}
