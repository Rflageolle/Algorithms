
from randomNumbers import BBSRandom
from math import sqrt, erfc
import scipy.stats as st
import numpy as np
import random, time
import matplotlib.pyplot as plt

rand = BBSRandom()

def frequency(r, alpha=0.01):
    """Frequency test, which determines whether the frequency of 1s and 0s in a
       randomly generated series of bits is near the expected frequency of 0.5

    Parameters
    ----------
    r : String or Bin
        randomly generated series of bits
    alpha : Double

    Returns
    -------
    Boolean
        returns whether to accept the null hypothesis that the series is random
    """

    hyp = True
    sum0 = 0
    sum1 = 1

    for i in r:
        if i == '0':
            sum0 += 1
        else:
            sum1 += 1

    sn = abs(sum1 - sum0)
    sobs = sn / (sqrt(len(r)))
    pvalue = erfc(sobs / sqrt(2))

    if pvalue < alpha:
        hyp = False

    return hyp


def checknextinsequence(r, i):
    if i < len(r) - 1:
        if r[i] == r[i + 1]:
            return True
    return False

def getsequence(r, i, seq):
    seq.append(r[i])
    if checknextinsequence(r, i):
        return getsequence(r, i + 1, seq)
    else:
        return (seq, i + 1)

def countruns(r, alpha=0.01):
    """Runs test is used to determine if a piece of data is from a random set.

    Parameters
    ----------
    r : String or Bin
        randomly generated series of bits
    alpha : Double

    Returns
    -------
    Boolean :
        returns whether to accept the null hypothesis that the series is random

    """
    hyp = True
    sum0 = 0
    sum1 = 1

    index = 0
    runs = []
    while index < len(r):
        seq, index = getsequence(r, index, [])
        runs.append(seq)

    for i in r:
        if i == '0':
            sum0 += 1
        else:
            sum1 += 1

    calc1 = float(2 * sum1 * sum0)
    calc2 = float(sum1 + sum0)

    expectedruns = calc1 / calc2 + 1

    variance = (calc1 * (calc1 - calc2)) / (calc2 * calc2 * (calc2 - 1))
    std = sqrt(variance)

    if std != 0:
        z = (len(runs) - expectedruns) / std
        pval = 2 * st.norm.sf(abs(z))

        if abs(z) > 1.96:
            hyp = False

        return hyp

    else: return False

def testMine(x):
    start = time.process_time()
    mine = [rand.getInt() for _ in range(x)]
    stop = time.process_time()

    return (mine , (stop - start))

def testPyth(x):
    start = time.process_time()
    pyth = [random.randint(0, 1024) for _ in range(x)]
    stop = time.process_time()

    return (pyth, (stop - start))

def runTests(x):
    mine, mtime = testMine(x)
    pyth, ptime = testPyth(x)

    mean_mine = np.mean(mine)
    mean_pyth = np.mean(pyth)

    std_mine = np.std(mine)
    std_pyth = np.std(pyth)

    frequency_percent_mine = 0
    frequency_percent_pyth = 0

    runs_percent_mine = 0
    runs_percent_pyth = 0

    for _ in range(100):
        if (frequency(rand.getBin(100))):
            frequency_percent_mine += 1
        if (frequency(str(bin(random.getrandbits(100))))):
            frequency_percent_pyth += 1
        if (countruns(rand.getBin(100))):
            runs_percent_mine += 1
        if (countruns(str(bin(random.getrandbits(100))))):
            runs_percent_pyth += 1

    print("Mean of my algorithm after 500000 runs is: ", mean_mine)
    print("Mean of built in python algorithm after 500000 runs is: ", \
        mean_pyth)

    print("Standard Deviation of my algorithm after 500000 runs is: %s" \
        % std_mine)
    print("Standard Deviation of built in python after 500000 runs is: %s" \
        % std_pyth)

    print("My algorithm passes the frequency test:", frequency_percent_mine)
    print("Built in python algorithm passes the frequency test:", \
        frequency_percent_pyth)

    print("My algorithm passes the runs test:", runs_percent_mine)
    print("Built in python algorithm passes the runs test:", runs_percent_pyth)

    print("My algorithm takes %s seconds per number" % (mtime / x))
    print("Built in python algorithm takes %s seconds per number" % (ptime / x))


def plotTests(name, color,  bins):
    plt.hist(name, color=color, edgecolor = 'black', bins=bins)
    plt.title('Random Numbers')
    plt.xlabel('Number')
    plt.ylabel('Frequency')
    plt.show()

runTests(500000)
# m = testMine()
# p = testPyth()
#
# plotTests(m, 'blue',750)
# plotTests(p, 'red', 750)
