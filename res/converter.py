from __future__ import division

import numpy as np

data = np.genfromtxt("fontHD.dat", dtype=str)

save_list = []

for i in range(0, len(data)):
	
	charid = data[i][0] + data[i][1]	
	save_list.append([charid, data[i][2], data[i][3], data[i][4], data[i][5], data[i][6], data[i][7], data[i][8], data[i][9], data[i][10]])
	
	print(save_list[i])
	
np.savetxt("fontHD_converted.dat", save_list, delimiter=",", fmt="%s")