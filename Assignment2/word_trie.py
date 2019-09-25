'''
    Author: Ryan Flageolle
    Assignment: To design a text prediction algorithm which prompts user input and
        returns the next 10 possible words from the input given
    Resources used: Learned about the Trie from:
        https://www.geeksforgeeks.org/trie-insert-and-search/
'''

'''
    class TrieNode - a node of the trie

    methods: __init__ - initializes a new node with an array of 26 None types
                        which acts as the children and will optionally be filled
                        as words are read in.
'''
class TrieNode:

    def __init__(self):
        self.children = [None]*26

        self.isEndOfWord = False

'''
    class Trie - the data structure used to save the suggested words

    methods: __init__ - initializes a trie object with an empty root node

             getNode - returns the current node

             _charToIndex - returns the index of a given letter. For Example: if
                            it is given an 'a' it will return 0

            insert - takes in a string and will run through the key and insert it
                     into the trie setting the leaf node's (of that word)
                     isEndOfWord property to True

            search - takes in a string and will run through the key and check if
                     it exists in the trie, returning true only if the last node
                     visited has a True for the isEndOfWord property

            getNextFullWord - will take in a character and return a word which
                              can be completed form that point, or None if there
                              is no word that can be created

            guess - will take in a character and return the next 10 words which
                    can be created, or None if there is no word that can be
                    created

'''
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

    def getNextFullWord(self):
        return stuff

    def search(self, key):
        current = self.root
        index = 0
        while (len(key) > index):
            node = ord(key[index].lower()) - ord('a')
            if (current.children[node] == None):
                return False
            else:
                print(chr(node + ord('a')))
                current = current.children[node]
                index = index + 1
        if (current.isEndOfWord):
            return True
        else:
            return "%s exists in the trie however it does not yet terminate" % key


    def guess(self, chars):
        options = []

        index = 0
        while (len(chars) > index):
            index = index + 1

'''
    methods: main - is used to create the trie object as well as reading in the


'''
def create_trie(file):
    t = Trie()

    x = 0
    with open(file) as f:
        for key in f:
            t.insert(key.lower().rstrip('\n'))
            x = x + 1

    print("successfully read in %d words from %s" % (x, file))
    return t

def main():
    trie = create_trie('words.txt')

    print(trie.search('aaa'))
    print(trie.search('abdominohysterectomy'))
    print(trie.search('fucky'))
    print(trie.search('subantiq'))

#remove_dups()
main()
