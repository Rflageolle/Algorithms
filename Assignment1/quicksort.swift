/*
Author: Ryan Flageolle
Date: 9/24/2019
Assignment: To analyse different implementations of quicksort to see which is
  the fastest.
Program: A group of methods which define different implementations of quicksort
  as well as our testing timer.
*/

import Foundation

/*
The base implementation of quicksort, written with the assistance of
https://github.com/raywenderlich/swift-algorithm-club/tree/master/Quicksort

quicksort() takes in any array of comparable objects and performs qicksort
returning the sorted array.

the partitioning is neither lomuto or hoare and is actually done using swifts
built in filter fuction.

the guard function is the atomic base for the recursive function
*/
func quicksort<T: Comparable>(_ a: [T]) -> [T] {
  guard a.count > 1 else { return a }

  let pivot = a[a.count/2]
  let less = a.filter { $0 < pivot }
  let equal = a.filter { $0 == pivot }
  let greater = a.filter { $0 > pivot }

  return quicksort(less) + equal + quicksort(greater)
}

/*
The base implementation of quicksort, written with the assistance of
https://github.com/raywenderlich/swift-algorithm-club/tree/master/Insertion%20Sort

insertionSort() takes in any array of comparable objects and performs insertion
sort on it, returning the sorted array.

the guard function protects from the case of an empty array being passed into the
function
*/
func insertionSort<T: Comparable>(_ array: [T]) -> [T] {
    guard array.count > 1 else { return array }

    var a = array
    for x in 1..<(a.count - 1) {
        var y = x
        while y > 0 && a[y] < a[y - 1] {
            a.swapAt(y , y - 1)
            y -= 1
        }
    }
    return a
}

/*
medianOfThree() takes in any array of comparable objects and performs quicksort
on it however it takes the median of the first, middle, and last elements to find
the pivot of the array, returning the sorted array.

the guard function protects from the case of an empty array being passed into the
function as well as acting as the atomic function for the recusive
*/
func medianOfThree<T: Comparable>(_ a: [T]) -> [T] {
  guard a.count > 1 else { return a }

  let arr = [a[0], a[(a.count) / 2], a[(a.count) - 1]]
  let pivot = arr.sorted(by: <)[2]
  let less = a.filter { $0 < pivot }
  let equal = a.filter { $0 == pivot }
  let greater = a.filter { $0 > pivot }

  return quicksort(less) + equal + quicksort(greater)
}

/*
bitterEnd() takes in any array of comparable objects and performs quicksort
on it however when the array being passed in less than x elements, which was
optimized at x=25, it then performs insertionSort on that array

becuase this function is recursive on only arrays larger than 25 elements and
uses insertionSort on smaller sections the guard function can be elited.
*/
func bitterEnd<T: Comparable>(_ a: [T]) -> [T] {

  if (a.count > 25) {
    let pivot = a[a.count/2]
    let less = a.filter { $0 < pivot }
    let equal = a.filter { $0 == pivot }
    let greater = a.filter { $0 > pivot }

    return bitterEnd(less) + equal + bitterEnd(greater)
  } else {
    return insertionSort(a)
  }

}

/*
partitionLomuto() takes in an array of Comparable objects, as well as two ints
which define the start and end of the 
*/
func partitionLomuto<T: Comparable>(_ a: inout [T], low: Int, high: Int) -> Int {
  let pivot = a[high]

  var i = low
  for j in low..<high {
    if a[j] <= pivot {
      (a[i], a[j]) = (a[j], a[i])
      i += 1
    }
  }

  (a[i], a[high]) = (a[high], a[i])
  return i
}

func quicksortLomuto<T: Comparable>(_ a: inout [T], low: Int, high: Int) {
  if low < high {
    let p = partitionLomuto(&a, low: low, high: high)
    quicksortLomuto(&a, low: low, high: p - 1)
    quicksortLomuto(&a, low: p + 1, high: high)
  }
}

func partitionHoare<T: Comparable>(_ a: inout [T], low: Int, high: Int) -> Int {
  let pivot = a[low]
  var i = low - 1
  var j = high + 1

  while true {
    repeat { j -= 1 } while a[j] > pivot
    repeat { i += 1 } while a[i] < pivot

    if i < j {
        //a.swapAt(i, j)
        (a[i], a[j]) = (a[j], a[i])
    } else {
      return j
    }
  }
}

func quicksortHoare<T: Comparable>(_ a: inout [T], low: Int, high: Int) {
  if low < high {
    let p = partitionHoare(&a, low: low, high: high)
    quicksortHoare(&a, low: low, high: p)
    quicksortHoare(&a, low: p + 1, high: high)
  }
}

func timeThatSort(f: String) {

  var toSort = Array(1...1000000).shuffled()
  let t1 = mach_absolute_time()

  switch f {
  case "median":
    let _ = medianOfThree(toSort)
  case "bitter":
    let _ = bitterEnd(toSort)
  case "lomuto":
    let _ = quicksortLomuto(&toSort, low: 0, high: toSort.count - 1)
  case "hoare":
    let _ = quicksortHoare(&toSort, low: 0, high: toSort.count - 1)
  default:
    let _ = quicksort(toSort)
  }

  let t2 = mach_absolute_time()

  let diff = t2 - t1
  var timeBaseInfo = mach_timebase_info_data_t()
  mach_timebase_info(&timeBaseInfo)
  let nanoS = Double(diff) * Double(timeBaseInfo.numer) / Double(timeBaseInfo.denom)

  print("\(f) completed the sort of 10,000 elements in \(nanoS * 0.000000001) seconds")

}

print(timeThatSort(f: "Base quicksort"))
print(timeThatSort(f: "median"))
print(timeThatSort(f: "bitter"))
print(timeThatSort(f: "lomuto"))
print(timeThatSort(f: "hoare"))
