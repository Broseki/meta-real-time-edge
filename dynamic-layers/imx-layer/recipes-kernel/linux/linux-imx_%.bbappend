
REAL_TIME_EDGE_LINUX_SRC ?= "git://github.com/nxp-real-time-edge-sw/real-time-edge-linux.git;protocol=https"
REAL_TIME_EDGE_LINUX_BRANCH ?= "linux_6.1.36"
REAL_TIME_EDGE_LINUX_SRCREV ?= "d9bfa1d513f1c3c516b1f6fb91d0eb243f92e3da"

KERNEL_SRC:real-time-edge = "${REAL_TIME_EDGE_LINUX_SRC};branch=${REAL_TIME_EDGE_LINUX_BRANCH}"
SRCBRANCH:real-time-edge = "${REAL_TIME_EDGE_LINUX_BRANCH}"
SRCREV:real-time-edge = "${REAL_TIME_EDGE_LINUX_SRCREV}"
SRC_URI = "${KERNEL_SRC}"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:real-time-edge = " \
    file://linux-baremetal.config \
    file://linux-baremetal-imx93.config \
"

do_configure:prepend:real-time-edge() {
    mkdir -p ${WORKDIR}/source-date-epoch
    date '+%s' > ${WORKDIR}/source-date-epoch/__source_date_epoch.txt
}
