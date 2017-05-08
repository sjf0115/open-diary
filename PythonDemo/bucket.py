import md5

def getBucket(uid):
    if len(uid)<5 or uid=='000000000000000':
        print 'error uid'
        return 'CE'
    src = uid.lower()
    m1 = md5.new()
    m1.update(src)
    dig=str(m1.hexdigest())
    print(dig)
    dig=dig[len(dig)-4:]
    print(dig)
    dig=int(dig,16)
    print dig
    print(float(dig)/65535.0)
    if(float(dig)/65535.0<0.1):
        return 'C1'
    elif(float(dig)/65535.0<0.2):
        return 'C2'
    #  elif(float(dig)/65535.0<0.3):
    #    return 'C4'
    else:
        return 'C3'

print getBucket('001EB876-AAA8-48DE-AB0B-EB5B35D542EE')