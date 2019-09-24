class TrieNode:

    def __init__(self):
        self.children = [None]*26

        self.isEndOfWord = False

class Trie:

    def __init__(self):
        self.root = self.getNode()

    def getNode(self):
        return TrieNode()

    def _charToIndex(self, ch):
        return ord(ch) - ord('a')

    def insert(self, key):
        start = self.root
        length = len(key)

        for level in range(length):
            index = self._charToIndex(key[level])
            if not start.children[index]:
                start.children[index] = self.getNode()
            start = start.children[index]

        start.isEndOfWord = True

def main():
    t = Trie()

    with open('words.txt') as file:
        x = 0
        for key in file:
            t.insert(key.lower().rstrip('\n'))
            x = x + 1

        print(x)


#remove_dups()
main()
