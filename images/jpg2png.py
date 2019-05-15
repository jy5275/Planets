import cv2
import sys
path = sys.argv[1]
img_rd = cv2.imread(path)
cv2.imwrite(sys.argv[2]+".png", img_rd)
