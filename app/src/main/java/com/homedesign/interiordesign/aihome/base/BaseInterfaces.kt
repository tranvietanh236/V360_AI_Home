package com.homedesign.interiordesign.aihome.base

/**
 * Interface để xử lý Loading, Error, Empty state
 * Có thể kế thừa thêm trong Activity
 */
interface ILoadingStateUI {
    /**
     * Hiển thị loading state
     */
    fun showLoading()

    /**
     * Ẩn loading state
     */
    fun hideLoading()

    /**
     * Hiển thị error state
     * @param message - thông báo lỗi
     * @param retryListener - listener khi nhấn retry (optional)
     */
    fun showError(message: String, retryListener: (() -> Unit)? = null)

    /**
     * Ẩn error state
     */
    fun hideError()

    /**
     * Hiển thị empty state
     * @param message - thông báo trống
     */
    fun showEmpty(message: String)

    /**
     * Ẩn empty state
     */
    fun hideEmpty()
}

/**
 * Interface cho permission handling
 */
interface IPermissionHandler {
    /**
     * Request permissions
     * @param permissions - danh sách permission cần request
     * @param requestCode - request code để phân biệt
     */
    fun requestPermissions(vararg permissions: String, requestCode: Int)

    /**
     * Kiểm tra permission đã được cấp
     */
    fun isPermissionGranted(permission: String): Boolean

    /**
     * Callback khi permission được cấp hoặc từ chối
     */
    fun onPermissionResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
}

