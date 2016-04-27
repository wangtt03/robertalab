candle lib.wxs firmware.wxs java.wxs setup.wxs
light -out OpenRobertaEV3.msi -ext WixUIExtension -cultures:de-DE lib.wixobj firmware.wixobj java.wixobj setup.wixobj -b ./lib -b ./firmware-1.2 -b ./java
@pause