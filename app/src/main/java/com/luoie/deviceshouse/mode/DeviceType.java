package com.luoie.deviceshouse.mode;

import java.io.Serializable;


public class DeviceType extends BaseDeviceType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int DEVICE_TYPE_ROBOT1  =  21;
	public static final int DEVICE_TYPE_ROBOT2  =  22;
	public static final int DEVICE_TYPE_ROBOT3  =  23;

	public static final int DEVICE_TYPE_BOILER_A  =  31;
	public static final int DEVICE_TYPE_BOILER_B  =  32;
	
	private String typeId;
	private int typeCode;
	private String typeName;
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public int getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}



	public static enum DevType{

		DEVICE_TYPE_ROBOT1("机器人", "0020"),
		DEVICE_TYPE_ROBOT2("机器人", "0021"),
		DEVICE_TYPE_ROBOT3("机器人", "0022");
		
		private String name;
		private String id;
		
		DevType(String name, String id){
			this.name = name;
			this.id = id;
		}
		
		private String getId(){
			return id;
		}
		
		public static String getName(String Id){
			for(DevType devType: DevType.values()){
				if(devType.getId().equals(Id)){
					return devType.name;
				}
			}
			return null;
		}
	}
}
