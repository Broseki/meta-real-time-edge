# Copyright 2021-2022 NXP

require recipes-bsp/u-boot/u-boot.inc
require real-time-edge-baremetal-env.inc
inherit fsl-u-boot-localversion

SUMMARY = "U-boot-baremetal provided by NXP"
LICENSE = "GPL-2.0-only & BSD-3-Clause & BSD-2-Clause & LGPL-2.0-only & LGPL-2.1-only"
LIC_FILES_CHKSUM = " \
    file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://Licenses/bsd-2-clause.txt;md5=6a31f076f5773aabd8ff86191ad6fdd5 \
    file://Licenses/bsd-3-clause.txt;md5=4a1190eac56a9db675d58ebe86eaf50c \
    file://Licenses/lgpl-2.0.txt;md5=4cf66a4984120007c9881cc871cf49db \
    file://Licenses/lgpl-2.1.txt;md5=4fbd65380cdd255951079008b364516c \
"

PROVIDES = "real-time-edge-baremetal"

DEPENDS:append = " libgcc dtc-native bison-native bc-native"

UBOOT_BAREMETAL_BRANCH ?= "baremetal-uboot_v2022.04"
UBOOT_BAREMETAL_SRC ?= "git://github.com/nxp-real-time-edge-sw/real-time-edge-uboot.git;protocol=https;"
SRC_URI = "${UBOOT_BAREMETAL_SRC};branch=${UBOOT_BAREMETAL_BRANCH}"

SRCREV = "2893cae2d872de6c96e14ff60db1d4e52d2eb8df"

PV = "2022.04+git${SRCPV}"

S = "${WORKDIR}/git"
B = "${S}"

UBOOT_BAREMETAL_DEFCONFIG ?= ""
DELTA_UBOOT_BAREMETAL_DEFCONFIG ?= ""
UBOOT_BAREMETAL_MAKE_TARGET ?= "all"
UBOOT_BAREMETAL_BINARY ?= "u-boot.bin"
UBOOT_BAREMETAL_RENAME ?= "bm-${UBOOT_BAREMETAL_BINARY}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_configure () {
    if [ -n "${UBOOT_BAREMETAL_DEFCONFIG}" ]; then
        cp ${S}/configs/${UBOOT_BAREMETAL_DEFCONFIG} ${B}/.config
        for deltacfg in ${DELTA_UBOOT_BAREMETAL_DEFCONFIG}; do
            ${S}/scripts/kconfig/merge_config.sh -m .config ${deltacfg}
        done
        cp ${B}/.config ${S}/configs/${UBOOT_BAREMETAL_DEFCONFIG}

        oe_runmake -C ${S} O=${B} ${UBOOT_BAREMETAL_DEFCONFIG}
    fi
    DEVTOOL_DISABLE_MENUCONFIG=true
}

do_compile () {
    if [ -n "${UBOOT_BAREMETAL_DEFCONFIG}" ]; then
        oe_runmake -C ${S} ${UBOOT_MAKE_TARGET}
        cp ${B}/${UBOOT_BINARY} ${B}/${UBOOT_BAREMETAL_RENAME}
    fi
}

do_install () {
    if [ -n "${UBOOT_BAREMETAL_DEFCONFIG}" ]; then
        install -D -m 644 ${B}/${UBOOT_BAREMETAL_RENAME} ${D}/boot/${UBOOT_BAREMETAL_RENAME}
    fi
}

do_deploy () {
    if [ -n "${UBOOT_BAREMETAL_DEFCONFIG}" ]; then
        install -D -m 644 ${B}/${UBOOT_BAREMETAL_RENAME} ${DEPLOYDIR}/${UBOOT_BAREMETAL_RENAME}
    fi
}

COMPATIBLE_MACHINE = "qoriq|imx"
