#!/bin/bash


compiler=''
program_name=''
program_location=''

SCRIPTPATH=$(dirname "$(readlink -f "$0")")
echo $SCRIPTPATH

compiler="$1"
program_name="$2"
program_location="$3"


include_paths="-I${SCRIPTPATH}/libs/microbit -I${SCRIPTPATH}/libs/microbit-dal -I${SCRIPTPATH}/libs/mbed-classic -I${SCRIPTPATH}/libs/ble -I${SCRIPTPATH}/libs/ble-nrf51822 -I${SCRIPTPATH}/libs/nrf51-sdk -I${SCRIPTPATH}/libs/microbit/inc -I${SCRIPTPATH}/libs/microbit-dal/inc/core -I${SCRIPTPATH}/libs/microbit-dal/inc/types -I${SCRIPTPATH}/libs/microbit-dal/inc/drivers -I${SCRIPTPATH}/libs/microbit-dal/inc/bluetooth -I${SCRIPTPATH}/libs/microbit-dal/inc/platform -I${SCRIPTPATH}/libs/mbed-classic/api -I${SCRIPTPATH}/libs/mbed-classic/hal -I${SCRIPTPATH}/libs/mbed-classic/targets/hal -I${SCRIPTPATH}/libs/mbed-classic/targets/cmsis -I${SCRIPTPATH}/libs/ble-nrf51822/source/btle -I${SCRIPTPATH}/libs/ble-nrf51822/source/btle/custom -I${SCRIPTPATH}/libs/ble-nrf51822/source/common -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/ble/ble_radio_notification -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/ble/ble_services/ble_dfu -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/ble/common -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/ble/device_manager -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/ble/device_manager/config -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/ble/peer_manager -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/device -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/ble_flash -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/delay -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/hal -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/pstorage -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/drivers_nrf/pstorage/config -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/bootloader_dfu -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/bootloader_dfu/hci_transport -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/crc16 -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/hci -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/scheduler -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/timer -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/util -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/fds -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/fstorage -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/libraries/experimental_section_vars -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/softdevice/common/softdevice_handler -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/softdevice/s130/headers -I${SCRIPTPATH}/libs/nrf51-sdk/source/nordic_sdk/components/toolchain -I${SCRIPTPATH}/libs/mbed-classic/targets -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822 -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/TARGET_NRF51_MICROBIT -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries/util -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries/scheduler -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/nordic_sdk/components/libraries/crc16 -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/s130_nrf51822_1_0_0 -I${SCRIPTPATH}/libs/mbed-classic/targets/hal/TARGET_NORDIC/TARGET_MCU_NRF51822/Lib/s110_nrf51822_8_0_0 -I${SCRIPTPATH}/libs/mbed-classic/targets/cmsis/TARGET_NORDIC -I${SCRIPTPATH}/libs/mbed-classic/targets/cmsis/TARGET_NORDIC/TARGET_MCU_NRF51822 -I${SCRIPTPATH}/libs/mbed-classic/targets/cmsis/TARGET_NORDIC/TARGET_MCU_NRF51822/TOOLCHAIN_GCC_ARM -I${SCRIPTPATH}/libs/mbed-classic/targets/cmsis/TARGET_NORDIC/TARGET_MCU_NRF51822/TOOLCHAIN_GCC_ARM/TARGET_MCU_NRF51_16K_S110 -Isource"
objecs="${program_location}target/${program_name}.cpp.o  ${SCRIPTPATH}/libs/microbit.a ${SCRIPTPATH}/libs/ble.a ${SCRIPTPATH}/libs/microbit-dal.a ${SCRIPTPATH}/libs/ble-nrf51822.a ${SCRIPTPATH}/libs/nrf51-sdk.a ${SCRIPTPATH}/libs/mbed-classic.a"


cppc_flags='-fno-exceptions -fno-unwind-tables -ffunction-sections -fdata-sections -Wno-unused-variable -Wall -Wextra -fno-rtti -fno-threadsafe-statics -mcpu=cortex-m0 -mthumb -D__thumb2__ -std=c++11 -fwrapv -Os -g -gdwarf-3 -DNDEBUG   -DTOOLCHAIN_GCC -DTOOLCHAIN_GCC_ARM -DMBED_OPERATORS -DNRF51 -DTARGET_NORDIC -DTARGET_M0 -D__MBED__=1 -DMCU_NORDIC_16K -DTARGET_NRF51_MICROBIT -DTARGET_MCU_NORDIC_16K -DTARGET_MCU_NRF51_16K_S110  -DTARGET_NRF_LFCLK_RC -DTARGET_MCU_NORDIC_16K -D__CORTEX_M0 -DARM_MATH_CM0 -MMD'
ld_flags='-fno-exceptions -fno-unwind-tables -Wl,--no-wchar-size-warning -Wl,--gc-sections -Wl,--sort-common -Wl,--sort-section=alignment -Wl,-wrap,main -mcpu=cortex-m0 -mthumb --specs=nano.specs'
ld_sys_libs='-lnosys  -lstdc++ -lsupc++ -lm -lc -lgcc -lstdc++ -lsupc++ -lm -lc -lgcc -Wl,'

echo "[1/3] Building the ${program_name}."
run="${compiler}arm-none-eabi-g++ ${include_paths} ${cppc_flags} -MT ${program_location}source/${program_name}.cpp.o -MF ${program_location}target/${program_name}.cpp.o.d -o ${program_location}target/${program_name}.cpp.o -c ${program_location}source/${program_name}.cpp"
if ! $run; then
    echo "Error in building !!!"
    exit 1
fi

echo "[2/3] Linking CXX executable"
run="${compiler}arm-none-eabi-g++ ${ld_flags} -T ${SCRIPTPATH}/libs/NRF51822.ld -Wl,-Map,${program_location}target/${program_name}.map -Wl,--start-group ${objecs} ${ld_sys_libs}--end-group -o ${program_location}target/${program_name}"
if ! $run; then
    echo "Error in linking !!!"
    exit 1
fi
echo "[3/3] Creating HEX file"
run="${compiler}arm-none-eabi-objcopy -O ihex ${program_location}target/${program_name} ${program_location}target/firmware.hex"
if ! $run; then
    echo "Error creating firmware.hex !!!"
    exit 1
fi
run="srec_cat ${SCRIPTPATH}/libs/BLE_BOOTLOADER_RESERVED.hex -intel ${SCRIPTPATH}/libs/s110_nrf51822_8.0.0_softdevice.hex -intel ${program_location}target/firmware.hex -intel -o ${program_location}target/${program_name}.hex -intel --line-length=44"
if ! $run; then
    echo "Error creating the HEX !!!"
    exit 1
fi
