FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append:real-time-edge = " \
    file://0001-ls1028ardb-rcw-Enable-CLK_OUT_PMUX-for-GPIO-function.patch \
    file://0002-ls1028ardb-enable-sai.patch \
"
