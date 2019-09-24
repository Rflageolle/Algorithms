import Foundation


class BTree {
  var root: BTreeNode

  init(r: BTreeNode<Key, Value) {
    self.root = r
  }

  
}

class BTreeNode<Key: Comparable, Value> {
  let degree = 26
  unowned var owner: BTreeNode<Key, Value>

  private var keys = [Key]()
  private var values = [Value]()
  var children: [BTreeNode]?

  var isLeaf: Bool {
    return children == nil
  }

  var numKeys: Int {
    return keys.count
  }

  init(owner: BTreeNode<Key, Value>) {
    self.owner = owner
  }

  convenience init(owner: BTreeNode<Key, Value>, keys: [Key],
                   values: [Value], children: [BTreeNode]? = nil) {
    self.init(owner: owner)
    self.keys += keys
    self.values += values
    self.children = children
  }

}

let file = "words.txt"
// create the destination url for the text file to be saved
do {
// Get the contents
let contents = try String(contentsOfFile: file, encoding: String.Encoding.ascii)
  print(contents)
} catch let error as NSError {
  print("Ooops! Something went wrong: (error)")
}
