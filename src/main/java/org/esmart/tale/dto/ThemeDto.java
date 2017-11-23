package org.esmart.tale.dto;

public class ThemeDto {

	/**
     * 主题名称
     */
    private String name;

    /**
     * 是否有设置项
     */
    private boolean hasSetting;

    public ThemeDto(String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHasSetting() {
		return hasSetting;
	}

	public void setHasSetting(boolean hasSetting) {
		this.hasSetting = hasSetting;
	}
    
}
