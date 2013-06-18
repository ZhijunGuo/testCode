/**
 * OnGetPictureListener.java
 * 2012-2-8
 */
package com.example.galaxy08.picture;

public interface OnGetPictureListener {
	/**
	 * @param data
	 *            File Bitmap Uri
	 */
	public void onGetPictureEnd(int requestCode, Object data);
}
