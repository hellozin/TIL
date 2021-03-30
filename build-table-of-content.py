import os

rootDir = "/Users/kakao/hellozin/TIL/basic"
baseLink = "https://github.com/hellozin/TIL/blob/master/basic"

baseDepth = rootDir.count("/")

for root, subdirs, files in os.walk(rootDir):
    baseLength = len(rootDir)
    baseDir = root[baseLength:]
    
    depth = root.count("/") - baseDepth

    dirname = root[root.rindex("/")+1:]
    print("\t"*depth + "- " + dirname)

    for file in files:
        print("{0}- [{1}]({2})".format("\t"*(depth+1), file, baseLink + baseDir + "/" + file))
        # print(baseLink + baseDir + "/" + file)