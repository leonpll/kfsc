package com.pactera

import java.util

import com.location.platform.common.protobuf.LocationPackage
import com.pactera.ReflectUtil.objectToMap
import com.pactera.utils.{MsgParserUtil, TimeUtils}

object ParserMap {


    /**
    * @Description: 解析消息封装map格式
    * * @param String
    * @Author: LL
    * @Date: 2019/10/16  10:05
    */
  def parserStr(msgStr: String): java.util.Map[String, Any] = {

    val returnMap = new util.HashMap[String, Any](300)

    val msg = MsgParserUtil.pasMsg(msgStr)

    val localMsg = LocationPackage.LocationMessage.parseFrom(msg.getMessage)

    // 解析location对象
    val localMap = objectToMap(localMsg.getLocation)
    returnMap.putAll(localMap)

    // 解析header对象
    val headerMap = ReflectUtil.objectToMap(localMsg.getHeader)
    returnMap.putAll(headerMap)

    // 解析carInfo对象
    val carInfoMap = ReflectUtil.objectToMap(localMsg.getCarInfo)
    returnMap.putAll(carInfoMap)

    // 解析carStatus对象
    val carStatusMap = ReflectUtil.objectToMap(localMsg.getCarStatus)
    returnMap.putAll(carStatusMap)

    // 解析EvInfo.evDearccWarning
    val evDearccWarnMap = ReflectUtil.objectToMap(localMsg.getEvInfo.getEvDearccWarning)
    returnMap.putAll(evDearccWarnMap)

    // 解析EvInfo.evLimitation
    val evLimitationMap = ReflectUtil.objectToMap(localMsg.getEvInfo.getEvLimitation)
    returnMap.putAll(evLimitationMap)

    // 解析EvInfo.evDate
    val evDateMap = ReflectUtil.objectToMap(localMsg.getEvInfo.getEvData)
    returnMap.putAll(evDateMap)

    // 解析EvInfo.EvEngine
    val evEvEngineMap = ReflectUtil.objectToMap(localMsg.getEvInfo.getEvEngine)
    returnMap.putAll(evEvEngineMap)

    // 解析EvInfo.evBattery
    val evBatteryMap = ReflectUtil.objectToMap(localMsg.getEvInfo.getEvBattery)
    returnMap.putAll(evBatteryMap)

    // 解析EvInfo.evWarning
    val evWarningMap = ReflectUtil.objectToMap(localMsg.getEvInfo.getEvWarning)
    returnMap.putAll(evWarningMap)


    // 转换集合类fenceTag
    val fenceTagList = localMsg.getWarningTag.getFenceTagList
    val stringLinkedListMap = ReflectUtil.listToMap(fenceTagList)
    returnMap.putAll(stringLinkedListMap)


    // 转换集合类getEvInfo.EvMotor.EvMotorDetail
    val evMotorDetailList = localMsg.getEvInfo.getEvMotor.getEvMotorDetailList
    val motorDetailMap = ReflectUtil.listToMap(evMotorDetailList)
    returnMap.put("evMotor_num", localMsg.getEvInfo.getEvMotor.getNum)
    returnMap.putAll(motorDetailMap)


    // 转换集合类getEvInfo.EvStorageV.EvStorageVoltage(singleBatteryV 字段需要单独转换)
    val evStorageVoltageList = localMsg.getEvInfo.getEvStorageV.getEvStorageVoltageList
    val singleBatteryVList = new util.LinkedList[Any]
    import scala.collection.JavaConversions._
    for (evStorageVoltage <- evStorageVoltageList) {
      val list = evStorageVoltage.getSingleBatteryVList
      singleBatteryVList.add(list)
    }
    val evStorageVoltageMap = ReflectUtil.listToMap(evStorageVoltageList)
    evStorageVoltageMap.put("evStorageVoltage_singleBatteryV", singleBatteryVList)
    returnMap.put("evStorageV_num", localMsg.getEvInfo.getEvStorageV.getNum)
    returnMap.putAll(evStorageVoltageMap)

    // 转换集合类getEvInfo.EevStorageT.EvStorageTemperature
    val evStorageTemperature = localMsg.getEvInfo.getEvStorageT.getEvStorageTemperatureList
    val evStorageTemperatureMap = ReflectUtil.listToMap(evStorageTemperature)
    returnMap.put("evStorageT_num", localMsg.getEvInfo.getEvStorageT.getNum)
    returnMap.putAll(evStorageTemperatureMap)

    // 预留字段规格
    returnMap.put("specs",1)

    // 添加写入时间
    returnMap.put("create_time", TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"))

    // 转换时间格式yyyy-MM-dd hh:mm:ss
    val localTimeLong : Long = returnMap.get("location_locationTime").asInstanceOf[Long]
    val localDateStr = TimeUtils.millis2String(localTimeLong, "yyyy-MM-dd");
    val localTimeStr = TimeUtils.millis2String(localTimeLong, "yyyy-MM-dd HH:mm:ss");
    returnMap.put("upload_date", localDateStr)
    returnMap.put("upload_time", localTimeStr)



    //    val id: Int = new Random().nextInt(1000000) + 1
    //
    //    val z = Array("1970-12-11", "1972-12-11", "1971-12-11", "1973-12-11", "1974-12-11", "1975-12-11", "1976-12-11", "1977-12-11", "1978-12-11", "1979-12-11",
    //      "1980-12-11", "1982-12-11", "1981-12-11", "1983-12-11", "1984-12-11", "1985-12-11", "1986-12-11", "1987-12-11", "1988-12-11", "1989-12-11",
    //      "1990-12-11", "1992-12-11", "1991-12-11", "1993-12-11", "1994-12-11", "1995-12-11", "1996-12-11", "1997-12-11", "1998-12-11", "1999-12-11")
    //
    //    user += ("id" -> id)
    //    user += ("username" -> "我是测试数据")
    //    user += ("realname" -> "我是测试数据1")
    //    user += ("birthday" -> z(new Random().nextInt(29) + 1))
    returnMap
  }


  /**
    * @Description: 解析消息封装map格式
    *               * @param String
    * @Author: LL
    * @Date: 2019/10/16  10:05
    */
  def parserStrGet(msgStr: String): java.util.Map[String, Any] = {

    val returnMap = new util.HashMap[String, Any](300)

    val msg = MsgParserUtil.pasMsg(msgStr)

    val localMsg = LocationPackage.LocationMessage.parseFrom(msg.getMessage)

    // 解析location对象
    returnMap.put("location_locationUuid", localMsg.getLocation.getLocationUuid)
    returnMap.put("location_terminalId", localMsg.getLocation.getTerminalId)
    returnMap.put("location_longitude", localMsg.getLocation.getLongitude)
    returnMap.put("location_latitude", localMsg.getLocation.getLatitude)
    returnMap.put("location_precision", localMsg.getLocation.getPrecision)
    returnMap.put("location_locationType", localMsg.getLocation.getLocationType)
    returnMap.put("location_areaPlace", localMsg.getLocation.getAreaPlace)
    returnMap.put("location_areaCode", localMsg.getLocation.getAreaCode)
    returnMap.put("location_areaName", localMsg.getLocation.getAreaName)
    returnMap.put("location_locationTime", localMsg.getLocation.getLocationTime)
    returnMap.put("location_timestampUTC", localMsg.getLocation.getTimestampUTC)
    returnMap.put("location_elevation", localMsg.getLocation.getElevation)
    returnMap.put("location_elevationPrecision", localMsg.getLocation.getElevationPrecision)
    returnMap.put("location_levelPrecision", localMsg.getLocation.getLevelPrecision)
    returnMap.put("location_satelliteNumbers", localMsg.getLocation.getSatelliteNumbers)
    returnMap.put("location_speed", localMsg.getLocation.getSpeed)
    returnMap.put("location_angle", localMsg.getLocation.getAngle)
    returnMap.put("location_extends", localMsg.getLocation.getExtends.toByteArray.mkString)
    returnMap.put("location_mncBitSrc", localMsg.getLocation.getMncBitSrc)
    returnMap.put("location_mnc", localMsg.getLocation.getMnc)
    returnMap.put("location_sid", localMsg.getLocation.getSid)
    returnMap.put("location_sid", localMsg.getLocation.getSid)
    returnMap.put("location_ladnid", localMsg.getLocation.getLadnid)
    returnMap.put("location_cidbid", localMsg.getLocation.getCidbid)
    returnMap.put("location_si", localMsg.getLocation.getSi)
    returnMap.put("location_hdop", localMsg.getLocation.getHdop)
    returnMap.put("location_vdop", localMsg.getLocation.getVdop)
    returnMap.put("location_tdop", localMsg.getLocation.getTdop)
    returnMap.put("location_messageType", localMsg.getLocation.getMessageType)
    returnMap.put("location_provinceCode", localMsg.getLocation.getProvinceCode)
    returnMap.put("location_provinceName", localMsg.getLocation.getProvinceName)
    returnMap.put("location_cityCode", localMsg.getLocation.getCityCode)
    returnMap.put("location_cityName", localMsg.getLocation.getCityName)
    returnMap.put("location_countryCode", localMsg.getLocation.getCountryCode)
    returnMap.put("location_countryName", localMsg.getLocation.getCountryName)
    returnMap.put("location_state", localMsg.getLocation.getState)
    returnMap.put("location_latDirection", localMsg.getLocation.getLatDirection)
    returnMap.put("location_lonDirection", localMsg.getLocation.getLonDirection)

    // 解析header对象
    returnMap.put("header_uuid", localMsg.getHeader.getUuid)
    returnMap.put("header_from", localMsg.getHeader.getFrom)
    returnMap.put("header_version", localMsg.getHeader.getVersion)
    returnMap.put("header_timestamp", localMsg.getHeader.getTimestamp)
    returnMap.put("header_identification", localMsg.getHeader.getIdentification)
    returnMap.put("header_status", localMsg.getHeader.getStatus)
    returnMap.put("header_description", localMsg.getHeader.getDescription)
    returnMap.put("header_terminalId", localMsg.getHeader.getTerminalId)
    returnMap.put("header_serviceId", localMsg.getHeader.getServiceId)
    returnMap.put("header_ptype", localMsg.getHeader.getPtype)
    returnMap.put("header_utype", localMsg.getHeader.getUtype)
    returnMap.put("header_userId", localMsg.getHeader.getUserId)


    // 解析carStatus对象
    returnMap.put("carStatus_warning1", localMsg.getCarStatus.getWarning1)
    returnMap.put("carStatus_warning2", localMsg.getCarStatus.getWarning2)
    returnMap.put("carStatus_hourOilconsumption", localMsg.getCarStatus.getHourOilconsumption)
    returnMap.put("carStatus_ageOilconsumption", localMsg.getCarStatus.getAgeOilconsumption)
    returnMap.put("carStatus_remainingOil", localMsg.getCarStatus.getRemainingOil)
    returnMap.put("carStatus_temperature", localMsg.getCarStatus.getTemperature)
    returnMap.put("carStatus_totalMileage", localMsg.getCarStatus.getTotalMileage)
    returnMap.put("carStatus_coolantTemperature", localMsg.getCarStatus.getCoolantTemperature)
    returnMap.put("carStatus_fuelCorrection", localMsg.getCarStatus.getFuelCorrection)
    returnMap.put("carStatus_revs", localMsg.getCarStatus.getRevs)
    returnMap.put("carStatus_speed", localMsg.getCarStatus.getSpeed)
    returnMap.put("carStatus_mileage", localMsg.getCarStatus.getMileage)
    returnMap.put("carStatus_throttleOpening", localMsg.getCarStatus.getThrottleOpening)
    returnMap.put("carStatus_totalOilconsumption", localMsg.getCarStatus.getTotalOilconsumption)
    returnMap.put("carStatus_motorPower", localMsg.getCarStatus.getMotorPower)
    returnMap.put("carStatus_batteryRemaining", localMsg.getCarStatus.getBatteryRemaining)
    returnMap.put("carStatus_chargingType", localMsg.getCarStatus.getChargingType)
    returnMap.put("carStatus_heatState", localMsg.getCarStatus.getHeatState)
    returnMap.put("carStatus_faultCodeArr", localMsg.getCarStatus.getFaultCodeArrList) //TODO1
    returnMap.put("carStatus_accuracy", localMsg.getCarStatus.getAccuracy)
    returnMap.put("carStatus_fbFlag", localMsg.getCarStatus.getFbFlag)
    returnMap.put("carStatus_fbAcceleration", localMsg.getCarStatus.getFbAcceleration)
    returnMap.put("carStatus_lrFlag", localMsg.getCarStatus.getLrFlag)
    returnMap.put("carStatus_lrAcceleration", localMsg.getCarStatus.getLrAcceleration)
    returnMap.put("carStatus_pdFlag", localMsg.getCarStatus.getPdFlag)
    returnMap.put("carStatus_pdAcceleration", localMsg.getCarStatus.getPdAcceleration)
    returnMap.put("carStatus_wheelAngle", localMsg.getCarStatus.getWheelAngle)
    returnMap.put("carStatus_wheelSpeed", localMsg.getCarStatus.getWheelSpeed)
    returnMap.put("carStatus_brakePedalState", localMsg.getCarStatus.getBrakePedalState)
    returnMap.put("carStatus_acceleratorPedalState", localMsg.getCarStatus.getAcceleratorPedalState)
    returnMap.put("carStatus_vehicleType", localMsg.getCarStatus.getVehicleType)
    returnMap.put("carStatus_stalls", localMsg.getCarStatus.getStalls)
    returnMap.put("carStatus_lfTyreP", localMsg.getCarStatus.getLfTyreP)
    returnMap.put("carStatus_lfTyreT", localMsg.getCarStatus.getLfTyreT)
    returnMap.put("carStatus_rfTyreP", localMsg.getCarStatus.getRfTyreP)
    returnMap.put("carStatus_rfTyreT", localMsg.getCarStatus.getRfTyreT)
    returnMap.put("carStatus_lbTyreP", localMsg.getCarStatus.getLbTyreP)
    returnMap.put("carStatus_lbTyreT", localMsg.getCarStatus.getLbTyreT)
    returnMap.put("carStatus_rbTyreP", localMsg.getCarStatus.getRbTyreP)
    returnMap.put("carStatus_rbTyreT", localMsg.getCarStatus.getRbTyreT)
    returnMap.put("carStatus_rainsSensorStatus", localMsg.getCarStatus.getRainsSensorStatus)
    returnMap.put("carStatus_accOnAngle", localMsg.getCarStatus.getAccOnAngle)
    returnMap.put("carStatus_batteryV", localMsg.getCarStatus.getBatteryV)
    returnMap.put("carStatus_dtcDelTime", localMsg.getCarStatus.getDtcDelTime)





    // 解析EvInfo.evDearccWarning对象
    returnMap.put("evDearccWarning_BMS_HVIL1", localMsg.getEvInfo.getEvDearccWarning.getBMSHVIL1)
    returnMap.put("evDearccWarning_BMS_HVIL2", localMsg.getEvInfo.getEvDearccWarning.getBMSHVIL2)
    returnMap.put("evDearccWarning_BMS_AlmLevel", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmLevel)
    returnMap.put("evDearccWarning_BMS_AlmOV", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmOV)
    returnMap.put("evDearccWarning_BMS_AlmUV", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmUV)
    returnMap.put("evDearccWarning_BMS_AlmOT", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmOT)
    returnMap.put("evDearccWarning_BMS_AlmUT", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmUT)
    returnMap.put("evDearccWarning_BMS_AlmDV", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmDV)
    returnMap.put("evDearccWarning_BMS_AlmDT", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmDT)
    returnMap.put("evDearccWarning_BMS_AlmOBV", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmOBV)
    returnMap.put("evDearccWarning_BMS_AlmUBV", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmUBV)
    returnMap.put("evDearccWarning_BMS_DCChargePlugTempOverHigh", localMsg.getEvInfo.getEvDearccWarning.getBMSDCChargePlugTempOverHigh)
    returnMap.put("evDearccWarning_BMS_AlmDschOcs", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmDschOcs)
    returnMap.put("evDearccWarning_BMS_AlmChrgOcs", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmChrgOcs)
    returnMap.put("evDearccWarning_BMS_AlmDschOct", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmDschOct)
    returnMap.put("evDearccWarning_BMS_AlmChrgOct", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmChrgOct)
    returnMap.put("evDearccWarning_BMS_AlmOffline", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmOffline)
    returnMap.put("evDearccWarning_BMS_AlmBsu", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmBsu)
    returnMap.put("evDearccWarning_BMS_AlmIso", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmIso)
    returnMap.put("evDearccWarning_BMS_AlmRelay", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmRelay)
    returnMap.put("evDearccWarning_BMS_AlmBmu", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmBmu)
    returnMap.put("evDearccWarning_BMS_AlmVcu", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmVcu)
    returnMap.put("evDearccWarning_BMS_AlmHV", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmHV)
    returnMap.put("evDearccWarning_BMS_AlmSenseCurr", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmSenseCurr)
    returnMap.put("evDearccWarning_BMS_AlmSenseCell", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmSenseCell)
    returnMap.put("evDearccWarning_BMS_AlmSenseTemp", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmSenseTemp)
    returnMap.put("evDearccWarning_BMS_AlmError", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmError)
    returnMap.put("evDearccWarning_BMS_AlmSOC", localMsg.getEvInfo.getEvDearccWarning.getBMSAlmSOC)
    returnMap.put("evDearccWarning_BMS_Mismatch", localMsg.getEvInfo.getEvDearccWarning.getBMSMismatch)
    returnMap.put("evDearccWarning_BMS_VCUOffline_F", localMsg.getEvInfo.getEvDearccWarning.getBMSVCUOfflineF)
    returnMap.put("evDearccWarning_BMS_SOCOverHigh", localMsg.getEvInfo.getEvDearccWarning.getBMSSOCOverHigh)
    returnMap.put("evDearccWarning_BMS_BattOverCharge", localMsg.getEvInfo.getEvDearccWarning.getBMSBattOverCharge)
    returnMap.put("evDearccWarning_BMS_SOCSkip", localMsg.getEvInfo.getEvDearccWarning.getBMSSOCSkip)
    returnMap.put("evDearccWarning_MCU_FaultLevel1", localMsg.getEvInfo.getEvDearccWarning.getMCUFaultLevel1)
    returnMap.put("evDearccWarning_MCU_FaultLevel2", localMsg.getEvInfo.getEvDearccWarning.getMCUFaultLevel2)
    returnMap.put("evDearccWarning_MCU_FaultLevel3", localMsg.getEvInfo.getEvDearccWarning.getMCUFaultLevel3)
    returnMap.put("evDearccWarning_MCU_SelfCheck_F", localMsg.getEvInfo.getEvDearccWarning.getMCUSelfCheckF)
    returnMap.put("evDearccWarning_MCU_TM_TempSensor_F", localMsg.getEvInfo.getEvDearccWarning.getMCUTMTempSensorF)
    returnMap.put("evDearccWarning_MCU_TMI_TempSensor_F", localMsg.getEvInfo.getEvDearccWarning.getMCUTMITempSensorF)
    returnMap.put("evDearccWarning_MCU_PhaseOverCurrent_F", localMsg.getEvInfo.getEvDearccWarning.getMCUPhaseOverCurrentF)
    returnMap.put("evDearccWarning_MCU_Inverter_F", localMsg.getEvInfo.getEvDearccWarning.getMCUInverterF)
    returnMap.put("evDearccWarning_MCU_DClinkOverCurrent_F", localMsg.getEvInfo.getEvDearccWarning.getMCUDClinkOverCurrentF)
    returnMap.put("evDearccWarning_MCU_SensorVoltage_F", localMsg.getEvInfo.getEvDearccWarning.getMCUSensorVoltageF)
    returnMap.put("evDearccWarning_MCU_MotorPositionSensor_F", localMsg.getEvInfo.getEvDearccWarning.getMCUMotorPositionSensorF)
    returnMap.put("evDearccWarning_MCU_IGTB_F", localMsg.getEvInfo.getEvDearccWarning.getMCUIGTBF)
    returnMap.put("evDearccWarning_MCU_DC_linkOverVoltage_F", localMsg.getEvInfo.getEvDearccWarning.getMCUDCLinkOverVoltageF)
    returnMap.put("evDearccWarning_MCU_DC_linkUnderVoltage_F", localMsg.getEvInfo.getEvDearccWarning.getMCUDCLinkUnderVoltageF)
    returnMap.put("evDearccWarning_MCU_MotorOverSpeed_F", localMsg.getEvInfo.getEvDearccWarning.getMCUMotorOverSpeedF)
    returnMap.put("evDearccWarning_MCU_Aphase_OverCurrentWarning", localMsg.getEvInfo.getEvDearccWarning.getMCUAphaseOverCurrentWarning)
    returnMap.put("evDearccWarning_MCU_Cphase_OverCurrentWarning", localMsg.getEvInfo.getEvDearccWarning.getMCUCphaseOverCurrentWarning)
    returnMap.put("evDearccWarning_MCU_MotorTemperatureWarning", localMsg.getEvInfo.getEvDearccWarning.getMCUMotorTemperatureWarning)
    returnMap.put("evDearccWarning_MCU_InverterTemperatureWarning", localMsg.getEvInfo.getEvDearccWarning.getMCUInverterTemperatureWarning)
    returnMap.put("evDearccWarning_MCU_Rx_CAN_F", localMsg.getEvInfo.getEvDearccWarning.getMCURxCANF)
    returnMap.put("evDearccWarning_MCU_LV_PowerSupply_F", localMsg.getEvInfo.getEvDearccWarning.getMCULVPowerSupplyF)
    returnMap.put("evDearccWarning_MCU_Safety_F", localMsg.getEvInfo.getEvDearccWarning.getMCUSafetyF)
    returnMap.put("evDearccWarning_MCU_ModeError", localMsg.getEvInfo.getEvDearccWarning.getMCUModeError)
    returnMap.put("evDearccWarning_MCU_GateDriverVoltageSupply_F", localMsg.getEvInfo.getEvDearccWarning.getMCUGateDriverVoltageSupplyF)
    returnMap.put("evDearccWarning_MCU_DC_linkOverCurrentwarning", localMsg.getEvInfo.getEvDearccWarning.getMCUDCLinkOverCurrentwarning)
    returnMap.put("evDearccWarning_VCU_ErrorLevel", localMsg.getEvInfo.getEvDearccWarning.getVCUErrorLevel)
    returnMap.put("evDearccWarning_VCU_WaterPumpError", localMsg.getEvInfo.getEvDearccWarning.getVCUWaterPumpError)
    returnMap.put("evDearccWarning_DCDC_OutputUnderVoltage_F", localMsg.getEvInfo.getEvDearccWarning.getDCDCOutputUnderVoltageF)
    returnMap.put("evDearccWarning_DCDC_OutputOverVoltage_F", localMsg.getEvInfo.getEvDearccWarning.getDCDCOutputOverVoltageF)
    returnMap.put("evDearccWarning_DCDC_InputUnderVoltage_F", localMsg.getEvInfo.getEvDearccWarning.getDCDCInputUnderVoltageF)
    returnMap.put("evDearccWarning_DCDC_InputOverVoltage_F", localMsg.getEvInfo.getEvDearccWarning.getDCDCInputOverVoltageF)
    returnMap.put("evDearccWarning_DCDC_Hardware_F", localMsg.getEvInfo.getEvDearccWarning.getDCDCHardwareF)
    returnMap.put("evDearccWarning_DCDC_COMOvertime_F", localMsg.getEvInfo.getEvDearccWarning.getDCDCCOMOvertimeF)
    returnMap.put("evDearccWarning_ICM_LowBrakeFluidLevel", localMsg.getEvInfo.getEvDearccWarning.getICMLowBrakeFluidLevel)
    returnMap.put("evDearccWarning_DCDC_OverTemperature_F", localMsg.getEvInfo.getEvDearccWarning.getDCDCOverTemperatureF)
    returnMap.put("evDearccWarning_VCU_BCAN_TortoiseLamp", localMsg.getEvInfo.getEvDearccWarning.getVCUBCANTortoiseLamp)
    returnMap.put("evDearccWarning_VCU_BCAN_LowBatterySts", localMsg.getEvInfo.getEvDearccWarning.getVCUBCANLowBatterySts)
    returnMap.put("evDearccWarning_VCU_BCAN_Fault", localMsg.getEvInfo.getEvDearccWarning.getVCUBCANFault)
    returnMap.put("evDearccWarning_OBC_Alarm_Level", localMsg.getEvInfo.getEvDearccWarning.getOBCAlarmLevel)
    returnMap.put("evDearccWarning_DCDC_Alarm_Level", localMsg.getEvInfo.getEvDearccWarning.getDCDCAlarmLevel)
    returnMap.put("evDearccWarning_MCU_InverterOverTemp_F", localMsg.getEvInfo.getEvDearccWarning.getMCUInverterOverTempF)


    // 解析EvInfo.evLimitation对象
    returnMap.put("evLimitation_hBatterySystemNum", localMsg.getEvInfo.getEvLimitation.getHBatterySystemNum)
    returnMap.put("evLimitation_hBatteryBodyNum", localMsg.getEvInfo.getEvLimitation.getHBatteryBodyNum)
    returnMap.put("evLimitation_hvBatteryBody", localMsg.getEvInfo.getEvLimitation.getHvBatteryBody)
    returnMap.put("evLimitation_lBatterySystemNum", localMsg.getEvInfo.getEvLimitation.getLBatterySystemNum)
    returnMap.put("evLimitation_lBatteryBodyNum", localMsg.getEvInfo.getEvLimitation.getLBatteryBodyNum)
    returnMap.put("evLimitation_lvBatteryBody", localMsg.getEvInfo.getEvLimitation.getLvBatteryBody)
    returnMap.put("evLimitation_hTemperatureSystemNum", localMsg.getEvInfo.getEvLimitation.getHTemperatureSystemNum)
    returnMap.put("evLimitation_hTemperature", localMsg.getEvInfo.getEvLimitation.getHTemperature)
    returnMap.put("evLimitation_lTemperatureSystemNum", localMsg.getEvInfo.getEvLimitation.getLTemperatureSystemNum)
    returnMap.put("evLimitation_lTemperature", localMsg.getEvInfo.getEvLimitation.getLTemperature)
    returnMap.put("evLimitation_lTemperatureProbeNum", localMsg.getEvInfo.getEvLimitation.getLTemperatureProbeNum)
    returnMap.put("evLimitation_hTemperatureProbeNum", localMsg.getEvInfo.getEvLimitation.getHTemperatureProbeNum)


    returnMap.put("carInfo_vin", localMsg.getCarInfo.getVin)

    // 解析EvInfo.evData对象
    returnMap.put("evData_carStatus", localMsg.getEvInfo.getEvData.getCarStatus)
    returnMap.put("evData_runMode", localMsg.getEvInfo.getEvData.getRunMode)
    returnMap.put("evData_chargingStatus", localMsg.getEvInfo.getEvData.getChargingStatus)
    returnMap.put("evData_speed", localMsg.getEvInfo.getEvData.getSpeed)
    returnMap.put("evData_totalM", localMsg.getEvInfo.getEvData.getTotalM)
    returnMap.put("evData_remainBatteryKmH", localMsg.getEvInfo.getEvData.getRemainBatteryKmH)
    returnMap.put("evData_totalV", localMsg.getEvInfo.getEvData.getTotalV)
    returnMap.put("evData_totalElectric", localMsg.getEvInfo.getEvData.getTotalElectric)
    returnMap.put("evData_insulationRes", localMsg.getEvInfo.getEvData.getInsulationRes)
    returnMap.put("evData_accPedalValue", localMsg.getEvInfo.getEvData.getAccPedalValue)
    returnMap.put("evData_brakePedalStatus", localMsg.getEvInfo.getEvData.getBrakePedalStatus)
    returnMap.put("evData_remainBattery", localMsg.getEvInfo.getEvData.getRemainBattery)
    returnMap.put("evData_noticeModel", localMsg.getEvInfo.getEvData.getNoticeModel)
    returnMap.put("evData_stalls", localMsg.getEvInfo.getEvData.getStalls.toByteArray.mkString)
    returnMap.put("evData_year", localMsg.getEvInfo.getEvData.getYear)
    returnMap.put("evData_soc", localMsg.getEvInfo.getEvData.getSoc)
    returnMap.put("evData_dc", localMsg.getEvInfo.getEvData.getDc)


    // 解析EvInfo.getEvBattery对象
    returnMap.put("evBattery_bV", localMsg.getEvInfo.getEvBattery.getBV)
    returnMap.put("evBattery_bElectric", localMsg.getEvInfo.getEvBattery.getBElectric)
    returnMap.put("evBattery_bFuelConsumptionRate", localMsg.getEvInfo.getEvBattery.getBFuelConsumptionRate)
    returnMap.put("evBattery_bProbeTotal", localMsg.getEvInfo.getEvBattery.getBProbeTotal)
    returnMap.put("evBattery_bProbeTemperature", localMsg.getEvInfo.getEvBattery.getBProbeTemperature.toByteArray)
    returnMap.put("evBattery_bHMaxTemperature", localMsg.getEvInfo.getEvBattery.getBHMaxTemperature)
    returnMap.put("evBattery_bHMaxProbeSeq", localMsg.getEvInfo.getEvBattery.getBHMaxProbeSeq)
    returnMap.put("evBattery_bHMaxConcentration", localMsg.getEvInfo.getEvBattery.getBHMaxConcentration)
    returnMap.put("evBattery_bHMaxCSensorSeq", localMsg.getEvInfo.getEvBattery.getBHMaxCSensorSeq)
    returnMap.put("evBattery_bHMaxPressure", localMsg.getEvInfo.getEvBattery.getBHMaxPressure)
    returnMap.put("evBattery_bHMaxPSensorSeq", localMsg.getEvInfo.getEvBattery.getBHMaxPSensorSeq)
    returnMap.put("evBattery_dc", localMsg.getEvInfo.getEvBattery.getDc)
    returnMap.put("evBattery_bSeq", localMsg.getEvInfo.getEvBattery.getBSeq)

    // 解析EvInfo.evEngine对象

    returnMap.put("evEngine_eStatus", localMsg.getEvInfo.getEvEngine.getEStatus)
    returnMap.put("evEngine_eCrankshaftRevs", localMsg.getEvInfo.getEvEngine.getECrankshaftRevs)
    returnMap.put("evEngine_eFuelConsumptionRate", localMsg.getEvInfo.getEvEngine.getEFuelConsumptionRate)


    // 解析EvInfo.evWarning对象
    returnMap.put("evWarning_eWLevel", localMsg.getEvInfo.getEvWarning.getEWLevel)
    returnMap.put("evWarning_eWFlag", localMsg.getEvInfo.getEvWarning.getEWFlag.toByteArray)
    returnMap.put("evWarning_eBatteryN1", localMsg.getEvInfo.getEvWarning.getEBatteryN1)
    returnMap.put("evWarning_eBatteryFaultCodes", localMsg.getEvInfo.getEvWarning.getEBatteryFaultCodes.toByteArray.mkString)
    returnMap.put("evWarning_eMotorN2", localMsg.getEvInfo.getEvWarning.getEMotorN2)
    returnMap.put("evWarning_eMotorN2FaultCodes", localMsg.getEvInfo.getEvWarning.getEMotorN2FaultCodes.toByteArray.mkString)
    returnMap.put("evWarning_eEngineN3", localMsg.getEvInfo.getEvWarning.getEEngineN3)
    returnMap.put("evWarning_eEngineN3FaultCodes", localMsg.getEvInfo.getEvWarning.getEEngineN3FaultCodes.toByteArray.mkString)
    returnMap.put("evWarning_eOtherN4", localMsg.getEvInfo.getEvWarning.getEOtherN4)
    returnMap.put("evWarning_eOtherN4FaultCodes", localMsg.getEvInfo.getEvWarning.getEOtherN4FaultCodes.toByteArray.mkString)
    returnMap.put("evWarning_eCompanyWFlag", localMsg.getEvInfo.getEvWarning.getECompanyWFlag.toByteArray)



    import scala.collection.JavaConversions._
    // 转换集合类fenceTag
    val fenceTagList = localMsg.getWarningTag.getFenceTagList
    val fenceUuidList = new util.LinkedList[Any]
    val statusList = new util.LinkedList[Any]
    val fenceTimeList = new util.LinkedList[Any]
    val longitudeList = new util.LinkedList[Any]
    val fenceRuleList = new util.LinkedList[Any]
    val fenceNameList = new util.LinkedList[Any]
    val terminalIdList = new util.LinkedList[Any]
    val latitudeList = new util.LinkedList[Any]

    for (fenceTag <- fenceTagList){
      fenceUuidList.add(fenceTag.getFenceUuid)
      statusList.add(fenceTag.getStatus)
      fenceTimeList.add(fenceTag.getFenceTime)
      longitudeList.add(fenceTag.getLongitude)
      fenceRuleList.add(fenceTag.getFenceRule)
      fenceNameList.add(fenceTag.getFenceName)
      terminalIdList.add(fenceTag.getTerminalId)
      latitudeList.add(fenceTag.getLatitude)
    }

    returnMap.put("fenceTag.fenceTag_fenceUuid", fenceUuidList);
    returnMap.put("fenceTag.fenceTag_status", statusList);
    returnMap.put("fenceTag.fenceTag_fenceTime", fenceTimeList);
    returnMap.put("fenceTag.fenceTag_longitude", longitudeList);
    returnMap.put("fenceTag.fenceTag_latitude", latitudeList);
    returnMap.put("fenceTag.fenceTag_terminalId", terminalIdList);
    returnMap.put("fenceTag.fenceTag_fenceName", fenceNameList);
    returnMap.put("fenceTag.fenceTag_fenceRule", fenceRuleList);


    // 转换集合类getEvInfo.EvMotor.EvMotorDetail
    returnMap.put("evMotor_num", localMsg.getEvInfo.getEvMotor.getNum)
    val evMotorDetailList = localMsg.getEvInfo.getEvMotor.getEvMotorDetailList

    val mSeqList = new util.LinkedList[Any]
    val mStatusList = new util.LinkedList[Any]
    val mConTemperatureList = new util.LinkedList[Any]
    val mRevsList = new util.LinkedList[Any]
    val mTorqueList = new util.LinkedList[Any]
    val mTemperatureList = new util.LinkedList[Any]
    val mInVList = new util.LinkedList[Any]
    val mElectricList = new util.LinkedList[Any]
    for (evMotorDetail <- evMotorDetailList){
      mSeqList.add(evMotorDetail.getMSeq)
      mStatusList.add(evMotorDetail.getMStatus)
      mConTemperatureList.add(evMotorDetail.getMConTemperature)
      mRevsList.add(evMotorDetail.getMRevs)
      mTorqueList.add(evMotorDetail.getMTorque)
      mTemperatureList.add(evMotorDetail.getMTemperature)
      mInVList.add(evMotorDetail.getMInV)
      mElectricList.add(evMotorDetail.getMElectric)
    }

    returnMap.put("evMotorDetail.evMotorDetail_mSeq", mSeqList);
    returnMap.put("evMotorDetail.evMotorDetail_mStatus", mStatusList);
    returnMap.put("evMotorDetail.evMotorDetail_mConTemperature", mConTemperatureList);
    returnMap.put("evMotorDetail.evMotorDetail_mRevs", mRevsList);
    returnMap.put("evMotorDetail.evMotorDetail_mTorque", mTorqueList);
    returnMap.put("evMotorDetail.evMotorDetail_mTemperature", mTemperatureList);
    returnMap.put("evMotorDetail.evMotorDetail_mInV", mInVList);
    returnMap.put("evMotorDetail.evMotorDetail_mElectric", mElectricList);



    // 转换集合类getEvInfo.EvStorageV.EvStorageVoltage(singleBatteryV 字段需要单独转换)
    val evStorageVoltageList = localMsg.getEvInfo.getEvStorageV.getEvStorageVoltageList
    returnMap.put("EvStorageV_num", localMsg.getEvInfo.getEvStorageV.getNum)
    val singleBatteryVList = new util.LinkedList[Any]
    val vseqList = new util.LinkedList[Any]
    val vsVoltageList = new util.LinkedList[Any]
    val vsElectricList = new util.LinkedList[Any]
    val vsBatteryCountList = new util.LinkedList[Any]
    val vsFrameBatteryNumList = new util.LinkedList[Any]
    val vsFrameBatteryCountList = new util.LinkedList[Any]

    for (evStorageVoltage <- evStorageVoltageList) {
      singleBatteryVList.add(evStorageVoltage.getSingleBatteryVList)
      vseqList.add(evStorageVoltage.getSeq)
      vsVoltageList.add(evStorageVoltage.getSVoltage)
      vsElectricList.add(evStorageVoltage.getSElectric)
      vsBatteryCountList.add(evStorageVoltage.getSBatteryCount)
      vsFrameBatteryNumList.add(evStorageVoltage.getSFrameBatteryNum)
      vsFrameBatteryCountList.add(evStorageVoltage.getSFrameBatteryCount)
    }
    returnMap.put("evStorageVoltage.evStorageVoltage_singleBatteryV", singleBatteryVList)
    returnMap.put("evStorageVoltage.evStorageVoltage_seq", vseqList)
    returnMap.put("evStorageVoltage.evStorageVoltage_sVoltage", vsVoltageList)
    returnMap.put("evStorageVoltage.evStorageVoltage_sElectric", vsElectricList)
    returnMap.put("evStorageVoltage.evStorageVoltage_sBatteryCount", vsBatteryCountList)
    returnMap.put("evStorageVoltage.evStorageVoltage_sFrameBatteryNum", vsFrameBatteryNumList)
    returnMap.put("evStorageVoltage.evStorageVoltage_sFrameBatteryCount", vsFrameBatteryCountList)


    // 转换集合类getEvInfo.EevStorageT.EvStorageTemperature
    returnMap.put("EevStorageT_num", localMsg.getEvInfo.getEvStorageT.getNum)
    val seqList = new util.LinkedList[Any]
    val sProbeTotalList = new util.LinkedList[Any]
    val sProbeTemperatureList = new util.LinkedList[Any]
    val evStorageTemperatureList = localMsg.getEvInfo.getEvStorageT.getEvStorageTemperatureList
    for (evStorageTemperature <- evStorageTemperatureList) {
      seqList.add(evStorageTemperature.getSeq)
      sProbeTotalList.add(evStorageTemperature.getSProbeTotal)
      sProbeTemperatureList.add(evStorageTemperature.getSProbeTemperature.toByteArray.mkString("[",",","]"))
    }
    returnMap.put("evStorageTemperature.evStorageTemperature_seq", seqList);
    returnMap.put("evStorageTemperature.evStorageTemperature_sProbeTotal", sProbeTotalList);
    returnMap.put("evStorageTemperature.evStorageTemperature_sProbeTemperature", sProbeTemperatureList);

    // 预留字段规格
    returnMap.put("specs",1)

    // 添加写入时间
    returnMap.put("create_time", TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"))

    // 转换时间格式yyyy-MM-dd hh:mm:ss
    val localTimeLong : Long = returnMap.get("location_locationTime").asInstanceOf[Long]
    val localDateStr = TimeUtils.millis2String(localTimeLong, "yyyy-MM-dd");
    val localTimeStr = TimeUtils.millis2String(localTimeLong, "yyyy-MM-dd HH:mm:ss");
    returnMap.put("upload_date", localDateStr)
    returnMap.put("upload_time", localTimeStr)

    returnMap
  }


  def main(args: Array[String]): Unit = {
    var z = Array("Runoob", "Baidu", "Google")
  }
}
