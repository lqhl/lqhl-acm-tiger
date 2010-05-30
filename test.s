###############Frag 0################
main:
move $30, $29
sub $29, $29, 320
L33:
sw $31, -52($30)
sw $23, -48($30)
sw $22, -44($30)
sw $21, -40($30)
li $2, 8
sw $2, -4($30)
add $2, $30, -8
sw $2, -248($30)
lw $2, -4($30)
li $8, 0
sw $4, -252($30)
sw $5, -256($30)
sw $3, -260($30)
sw $3, -200($30)
sw $3, -164($30)
sw $3, -128($30)
sw $3, -92($30)
sw $6, -56($30)
sw $7, -24($30)
move $4, $2
move $5, $8
jal _initArray
add $30, $29, 320
lw $4, -252($30)
lw $5, -256($30)
lw $6, -56($30)
lw $3, -92($30)
lw $3, -128($30)
lw $3, -164($30)
lw $3, -200($30)
lw $3, -260($30)
lw $7, -24($30)
lw $3, -248($30)
sw $2, 0($3)
add $2, $30, -12
sw $2, -244($30)
lw $2, -4($30)
li $8, 0
sw $4, -264($30)
sw $5, -268($30)
sw $29, -272($30)
sw $29, -208($30)
sw $29, -172($30)
sw $29, -136($30)
sw $29, -100($30)
sw $6, -64($30)
sw $7, -28($30)
move $4, $2
move $5, $8
jal _initArray
add $30, $29, 320
lw $4, -264($30)
lw $5, -268($30)
lw $6, -64($30)
lw $3, -100($30)
lw $3, -136($30)
lw $3, -172($30)
lw $3, -208($30)
lw $3, -272($30)
lw $7, -28($30)
lw $3, -244($30)
sw $2, 0($3)
add $2, $30, -16
sw $2, -240($30)
lw $8, -4($30)
lw $2, -4($30)
add $2, $8, $2
sub $2, $2, 1
li $8, 0
sw $4, -276($30)
sw $5, -280($30)
sw $29, -284($30)
sw $29, -216($30)
sw $29, -180($30)
sw $29, -144($30)
sw $29, -108($30)
sw $6, -72($30)
sw $7, -32($30)
move $4, $2
move $5, $8
jal _initArray
add $30, $29, 320
lw $4, -276($30)
lw $5, -280($30)
lw $6, -72($30)
lw $3, -108($30)
lw $3, -144($30)
lw $3, -180($30)
lw $3, -216($30)
lw $3, -284($30)
lw $7, -32($30)
lw $3, -240($30)
sw $2, 0($3)
add $2, $30, -20
sw $2, -236($30)
lw $2, -4($30)
lw $8, -4($30)
add $2, $2, $8
sub $2, $2, 1
li $8, 0
sw $4, -288($30)
sw $5, -292($30)
sw $29, -296($30)
sw $29, -224($30)
sw $29, -188($30)
sw $29, -152($30)
sw $29, -116($30)
sw $6, -80($30)
sw $7, -36($30)
move $4, $2
move $5, $8
jal _initArray
add $30, $29, 320
lw $4, -288($30)
lw $5, -292($30)
lw $6, -80($30)
lw $3, -116($30)
lw $3, -152($30)
lw $3, -188($30)
lw $3, -224($30)
lw $3, -296($30)
lw $7, -36($30)
lw $3, -236($30)
sw $2, 0($3)
li $2, 0
sw $4, -300($30)
sw $5, -304($30)
sw $6, -308($30)
sw $29, -312($30)
sw $29, -232($30)
sw $29, -196($30)
sw $29, -160($30)
sw $29, -124($30)
sw $7, -88($30)
sw $30, 0($29)
move $4, $2
jal try
add $30, $29, 320
lw $4, -300($30)
lw $5, -304($30)
lw $6, -308($30)
lw $7, -88($30)
lw $2, -124($30)
lw $2, -160($30)
lw $2, -196($30)
lw $2, -232($30)
lw $2, -312($30)
lw $21, -40($30)
lw $22, -44($30)
lw $23, -48($30)
lw $31, -52($30)
j L32
L32:
move $29, $30
j exit
###############Frag 1################
try:
move $30, $29
sub $29, $29, 68
L35:
sw $4, -28($30)
sw $31, -24($30)
sw $23, -20($30)
sw $22, -16($30)
sw $21, -12($30)
lw $2, 0($30)
lw $2, -4($2)
lw $3, -28($30)
beq $3, $2, L15
L16:
li $2, 0
sw $2, -8($30)
lw $2, 0($30)
lw $2, -4($2)
sub $2, $2, 1
sw $2, -4($30)
lw $2, -4($30)
lw $3, -8($30)
ble $3, $2, L17
L13:
L14:
lw $21, -12($30)
lw $22, -16($30)
lw $23, -20($30)
lw $31, -24($30)
j L34
L15:
lw $2, 0($30)
sw $4, -52($30)
sw $5, -56($30)
sw $6, -60($30)
sw $7, -64($30)
sw $2, 0($29)
jal printboard
add $30, $29, 68
lw $4, -52($30)
lw $5, -56($30)
lw $6, -60($30)
lw $7, -64($30)
j L14
L17:
lw $2, 0($30)
lw $2, -8($2)
li $8, 4
lw $3, -8($30)
mul $8, $3, $8
add $2, $2, $8
lw $2, 0($2)
sub $2, $2, 0
beqz $2, L26
L27:
li $2, 0
L25:
sub $2, $2, 0
bnez $2, L23
L24:
li $2, 0
L22:
sub $2, $2, 0
bnez $2, L20
L19:
lw $2, -4($30)
lw $3, -8($30)
bge $3, $2, L13
L18:
lw $2, -8($30)
add $2, $2, 1
sw $2, -8($30)
j L17
L26:
li $2, 1
lw $8, 0($30)
lw $8, -16($8)
lw $9, -8($30)
lw $3, -28($30)
add $9, $9, $3
li $10, 4
mul $9, $9, $10
add $8, $8, $9
lw $8, 0($8)
sub $8, $8, 0
beqz $8, L28
L29:
li $2, 0
L28:
j L25
L23:
li $2, 1
lw $8, 0($30)
lw $8, -20($8)
lw $3, -8($30)
add $9, $3, 7
lw $3, -28($30)
sub $9, $9, $3
li $10, 4
mul $9, $9, $10
add $8, $8, $9
lw $8, 0($8)
sub $8, $8, 0
beqz $8, L30
L31:
li $2, 0
L30:
j L22
L20:
lw $2, 0($30)
lw $2, -8($2)
li $8, 4
lw $3, -8($30)
mul $8, $3, $8
add $2, $2, $8
li $8, 1
sw $8, 0($2)
lw $2, 0($30)
lw $2, -16($2)
lw $8, -8($30)
lw $3, -28($30)
add $8, $8, $3
li $9, 4
mul $8, $8, $9
add $2, $2, $8
li $8, 1
sw $8, 0($2)
lw $2, 0($30)
lw $2, -20($2)
lw $3, -8($30)
add $8, $3, 7
lw $3, -28($30)
sub $8, $8, $3
li $9, 4
mul $8, $8, $9
add $2, $2, $8
li $8, 1
sw $8, 0($2)
lw $2, 0($30)
lw $2, -12($2)
li $8, 4
lw $3, -28($30)
mul $8, $3, $8
add $2, $2, $8
lw $3, -8($30)
sw $3, 0($2)
lw $8, 0($30)
lw $2, -28($30)
add $2, $2, 1
sw $4, -36($30)
sw $5, -40($30)
sw $6, -44($30)
sw $7, -48($30)
sw $8, 0($29)
move $4, $2
jal try
add $30, $29, 68
lw $4, -36($30)
lw $5, -40($30)
lw $6, -44($30)
lw $7, -48($30)
lw $2, 0($30)
lw $2, -8($2)
li $8, 4
lw $3, -8($30)
mul $8, $3, $8
add $2, $2, $8
li $8, 0
sw $8, 0($2)
lw $2, 0($30)
lw $2, -16($2)
lw $8, -8($30)
lw $3, -28($30)
add $8, $8, $3
li $9, 4
mul $8, $8, $9
add $2, $2, $8
li $8, 0
sw $8, 0($2)
lw $2, 0($30)
lw $2, -20($2)
lw $3, -8($30)
add $8, $3, 7
lw $3, -28($30)
sub $8, $8, $3
li $9, 4
mul $8, $8, $9
add $2, $2, $8
li $8, 0
sw $8, 0($2)
j L19
L34:
move $29, $30
jr $31
###############Frag 2################
printboard:
move $30, $29
sub $29, $29, 88
L37:
sw $31, -32($30)
sw $23, -28($30)
sw $22, -24($30)
sw $21, -20($30)
sw $20, -16($30)
li $2, 0
sw $2, -12($30)
lw $2, 0($30)
lw $2, -4($2)
sub $2, $2, 1
sw $2, -8($30)
lw $2, -8($30)
lw $3, -12($30)
ble $3, $2, L11
L0:
lw $2, 0($30)
lw $8, 0($2)
la $2, L10
sw $4, -72($30)
sw $5, -76($30)
sw $6, -80($30)
sw $7, -84($30)
sw $8, 0($29)
move $4, $2
jal print
add $30, $29, 88
lw $4, -72($30)
lw $5, -76($30)
lw $6, -80($30)
lw $7, -84($30)
lw $20, -16($30)
lw $21, -20($30)
lw $22, -24($30)
lw $23, -28($30)
lw $31, -32($30)
j L36
L11:
li $2, 0
sw $2, -36($30)
lw $2, 0($30)
lw $2, -4($2)
sub $2, $2, 1
sw $2, -4($30)
lw $8, -4($30)
lw $2, -36($30)
ble $2, $8, L8
L1:
lw $2, 0($30)
lw $8, 0($2)
la $2, L7
sw $4, -56($30)
sw $5, -60($30)
sw $6, -64($30)
sw $7, -68($30)
sw $8, 0($29)
move $4, $2
jal print
add $30, $29, 88
lw $4, -56($30)
lw $5, -60($30)
lw $6, -64($30)
lw $7, -68($30)
lw $2, -8($30)
lw $3, -12($30)
bge $3, $2, L0
L12:
lw $2, -12($30)
add $2, $2, 1
sw $2, -12($30)
j L11
L8:
lw $2, 0($30)
lw $9, 0($2)
lw $2, 0($30)
lw $2, -12($2)
li $8, 4
lw $3, -12($30)
mul $8, $3, $8
add $2, $2, $8
lw $8, 0($2)
lw $2, -36($30)
beq $8, $2, L5
L6:
la $2, L3
L4:
sw $4, -40($30)
sw $5, -44($30)
sw $6, -48($30)
sw $7, -52($30)
sw $9, 0($29)
move $4, $2
jal print
add $30, $29, 88
lw $4, -40($30)
lw $5, -44($30)
lw $6, -48($30)
lw $7, -52($30)
lw $8, -4($30)
lw $2, -36($30)
bge $2, $8, L1
L9:
lw $2, -36($30)
add $2, $2, 1
sw $2, -36($30)
j L8
L5:
la $2, L2
j L4
L36:
move $29, $30
jr $31
###############Frag 3################
.data
L10:.asciiz "\n"
.text
###############Frag 4################
.data
L7:.asciiz "\n"
.text
###############Frag 5################
.data
L3:.asciiz " ."
.text
###############Frag 6################
.data
L2:.asciiz " O"
.text

.text
_initArray:
	li $at, 4
	mul $a0, $a0, $at
	li $v0, 9
	syscall
	move $v1, $v0
	add $a0, $a0, $v0
	_initArray_0:
	sw $a1, ($v1)
	add $v1, $v1, 4
	bne $v1, $a0, _initArray_0
	jr $ra

printi:
    li $v0, 1
    syscall
    jr $ra

print:
    li $v0, 4
    syscall
    jr $ra

flush:
    jr $ra

strcmp:
    strcmptest:
    lb $a2 ($a0)
    lb $a3 ($a1)
    beq $a2, $zero, strcmpend
    beq $a3, $zero, strcmpend
    bgt $a2, $a3  strcmpgreat
    blt $a2, $a3  strcmpless
    add $a0, $a0, 1
    add $a1, $a1, 1
    j strcmptest
    strcmpgreat:
    li $v0, 1
    jr $ra
    strcmpless:
    li $v0, -1
    jr $ra
    strcmpend:
    bne $a2 $zero strcmpgreat
    bne $a3 $zero strcmpless
    li $v0, 0
    jr $ra

size:
    move $v0, $zero
    sizeloop:
    lb $a1 ($a0)
    beq $a1, $zero sizeexit
    add $v0, $v0, 1
    add $a0, $a0, 1
    j sizeloop
    sizeexit:
    jr $ra

ord:
    lb $a1,($a0)
    li $v0,-1
    beqz $a1,Lrunt5
    lb $v0,($a0)
    Lrunt5:
    jr $ra

getchar:
    li $v0, 9 
    li $a0, 2
    syscall
    move $a0, $v0
    li $a1, 2
    li $v0, 8
    syscall
    move $v0, $a0
    jr $ra

chr:
    move $a1, $a0
    li $v0, 9
    li $a0, 2
    syscall
    sb $a1 ($v0)
    sb $zero 1($v0)
    jr $ra

exit:
    li $v0, 10
    syscall

substring:
    add $a1, $a0, $a1
    move $a3, $a1
    li $v0, 9
    add $a2, $a2, 1
    move $a0, $a2
    add $a0, $a0, 1 
    syscall
    # got a new string in $v0
    add $a2,$a2,$a3
    add $a2,$a2,-1
    move $a0, $v0
    substringcopy:
    beq $a1 $a2 substringexit
    lb $a3 ($a1)
    sb $a3 ($a0)
    add $a1, $a1, 1
    add $a0, $a0, 1 
    j substringcopy
    substringexit:
    sb $zero, ($a0)
    jr $ra

copy:
    copyloop:
    lb $a2, ($a1)
    beq $zero, $a2 copyexit 
    sb $a2, ($a0)   
    add $a0,$a0,1
    add $a1,$a1,1
    j copyloop
    copyexit:
    sb $zero, ($a0)
    move $v0, $a0
    jr $ra

concat:
    sw $a0, -4($sp)
    sw $a1, -8($sp)
    sw $ra, -12($sp)
    jal size
    li $a3, 1
    add $a3,$a3,$v0
    lw $a0, -8($sp)
    jal size
    add $a3, $a3, $v0
    move $a0, $a3
    li $v0, 9
    syscall 
    move $a3, $v0
    move $a0, $v0
    lw   $a1, -4($sp)
    jal copy
    move $a0, $v0
    lw $a1, -8($sp)
    jal copy
    move $v0, $a3
    lw $ra, -12($sp)
    jr $ra
