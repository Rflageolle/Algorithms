import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

x = np.linspace(0, 10, 1001)

f = lambda x: np.abs(np.sin(2 * np.pi * x) ** 5 - 2 *\
                     np.cos(3 * np.cos(x / np.pi) ** 2) ** 3)

g = lambda x: np.abs(3 * (np.cos(x)) + (np.sin(x)))

plt.figure()
plt.plot(x, g(x), linewidth = 3)
plt.show()

# Define number of samples
N = 100000

# Define rectangle boundaries
rec_x = [0, 10]
rec_y = [0, 3]
# Sample from uniform distribution
mc_x = np.random.uniform(min(rec_x), max(rec_x), N)
mc_y = np.random.uniform(min(rec_y), max(rec_y), N)

# Define the points under the curve
points_under = [True if mc_y[i] <= f(mc_x[i]) else False
                for i in range(len(mc_x))]

# Plot the points
plt.figure()
plt.plot(x, f(x), linewidth = 5, c = 'k')
plt.scatter(mc_x[points_under], mc_y[points_under],
            c = 'r', s = 15)
plt.xlim(rec_x)
plt.ylim(rec_y)
plt.show()

# Calculate integral
integral = ((max(rec_x) - min(rec_x)) *
            (max(rec_y) - min(rec_y)) * sum(points_under) / N)
print("Integral estimate: %.2f" %integral)
