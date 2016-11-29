echo off

set compiler=%1
set program_name=%2
set program_location=%3

set include_paths=-I%~dp0/libs/microbit -I%~dp0/libs/microbit-dal -I%~dp0/libs/mbed-classic -I%~dp0/libs/ble -I%~dp0/libs/ble-nrf51822 -I%~dp0/libs/nrf51-sdk -I%~dp0/libs/microbit/inc -I%~dp0/libs/microbit-dal/inc/core -I%~dp0/libs/microbit-dal/inc/types -I%~dp0/libs/microbit-dal/inc/drivers -I%~dp0/libs/microbit-dal/inc/bluetooth -I%~dp0/libs/microbit-dal/inc/platform -I%~dp0/libs/mbed-classic/api -I%~dp0/libs/mbed-classic/hal -I%~dp0/libs/mbed-classic/targets/hal -I%~dp0/libs/mbed-classic/targets/cmsis -I%~dp0/libs/ble-nrf51822/source/btle -I%~dp0/libs/ble-nrf51822/source/btle/custom -I%~dp0/libs/ble-nrf51822/source/common -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/ble/ble_radio_notification -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/ble/ble_services/ble_dfu -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/ble/common -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/ble/device_manager -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/ble/device_manager/config -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/ble/peer_manager -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/device -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/ble_flash -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/delay -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/hal -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/pstorage -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/pstorage/config -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/bootloader_dfu -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/bootloader_dfu/hci_transport -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/crc16 -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/hci -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/scheduler -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/timer -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/util -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/fds -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/fstorage -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/libraries/experimental_section_vars -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/softdevice/common/softdevice_handler -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/softdevice/s130/headers -I%~dp0/libs/nrf51-sdk/source/nordic_sdk/components/toolchain -I%~dp0/libs/mbed-classic/targets -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822 -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/TARGET_NRF51_MICROBIT -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries/util -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries/scheduler -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries/crc16 -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/s130_nrf51822_1_0_0 -I%~dp0/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/s110_nrf51822_8_0_0 -I%~dp0/libs/mbed-classic/targets/cmsis/TARGET_NORDIC -I%~dp0/libs/mbed-classic/targets/cmsis/TARGET_NORDIC/TARGET_MCU_NRF51822 -I%~dp0/libs/mbed-classic/targets/cmsis/TARGET_NORDIC/TARGET_MCU_NRF51822/TOOLCHAIN_GCC_ARM -I%~dp0/libs/mbed-classic/targets/cmsis/TARGET_NORDIC/TARGET_MCU_NRF51822/TOOLCHAIN_GCC_ARM/TARGET_MCU_NRF51_16K_S110 -Isource
set objecs=%program_location%target/%program_name%.cpp.o  %~dp0/libs/microbit.a %~dp0/libs/ble.a %~dp0/libs/microbit-dal.a %~dp0/libs/ble-nrf51822.a %~dp0/libs/nrf51-sdk.a %~dp0/libs/mbed-classic.a


set cppc_flags=-fno-exceptions -fno-unwind-tables -ffunction-sections -fdata-sections -Wno-unused-variable -Wall -Wextra -fno-rtti -fno-threadsafe-statics -mcpu=cortex-m0 -mthumb -D__thumb2__ -std=c++11 -fwrapv -Os -g -gdwarf-3 -DNDEBUG   -DTOOLCHAIN_GCC -DTOOLCHAIN_GCC_ARM -DMBED_OPERATORS -DNRF51 -DTARGET_NORDIC -DTARGET_M0 -D__MBED__=1 -DMCU_NORDIC_16K -DTARGET_NRF51_MICROBIT -DTARGET_MCU_NORDIC_16K -DTARGET_MCU_NRF51_16K_S110  -DTARGET_NRF_LFCLK_RC -DTARGET_MCU_NORDIC_16K -D__CORTEX_M0 -DARM_MATH_CM0 -MMD
set ld_flags=-fno-exceptions -fno-unwind-tables -Wl,--no-wchar-size-warning -Wl,--gc-sections -Wl,--sort-common -Wl,--sort-section=alignment -Wl,-wrap,main -mcpu=cortex-m0 -mthumb --specs=nano.specs
set ld_sys_libs=-lnosys  -lstdc++ -lsupc++ -lm -lc -lgcc -lstdc++ -lsupc++ -lm -lc -lgcc -Wl,

echo "[1/3] Building the %program_name%."

%compiler%arm-none-eabi-g++ %include_paths% %cppc_flags% -MT %program_location%source/%program_name%.cpp.o -MF %program_location%target/%program_name%.cpp.o.d -o %program_location%target/%program_name%.cpp.o -c %program_location%source/%program_name%.cpp 

IF %errorlevel% NEQ 0 (
	echo "Error in building !!!"
    exit 1
)

echo "[2/3] Linking CXX executable"
%compiler%arm-none-eabi-g++ %ld_flags% -T %~dp0/libs/NRF51822.ld -Wl,-Map,%program_location%target/%program_name%.map -Wl,--start-group %objecs% %ld_sys_libs%--end-group -o %program_location%target/%program_name%

IF %errorlevel% NEQ 0 (
	echo "Error in linking !!!"
    exit 1
)

echo "[3/3] Creating HEX file"
%compiler%arm-none-eabi-objcopy -O ihex %program_location%target/%program_name% %program_location%target/firmware.hex
IF %errorlevel% NEQ 0 (
    echo "Error creating firmware.hex !!!"
    exit 1
)

srec_cat %~dp0/libs/BLE_BOOTLOADER_RESERVED.hex -intel %~dp0/libs/s110_nrf51822_8.0.0_softdevice.hex -intel %program_location%target/firmware.hex -intel -o %program_location%target/%program_name%.hex -intel --line-length=44

IF %errorlevel% NEQ 0 (
    echo "Error creating the HEX !!!"
    exit 1
)
