package br.com.prevent.logmanager.repository.domain;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<T> content;
    private int number;
    private boolean first;
    private boolean last;
    private int size;
    private int totalPages;
    private int totalElements;
    
	public Page() {
	}
	
	public Page(List<T> content, int number, boolean first, boolean last, int size, int totalPages, int totalElements) {
		super();
		this.content = content;
		this.number = number;
		this.first = first;
		this.last = last;
		this.size = size;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
	}
	
	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public String toString() {
		return "Page [number=" + number + ", first=" + first + ", last=" + last + ", size="
				+ size + ", totalPages=" + totalPages + ", totalElements=" + totalElements + ", content=" + content;
	}

}
