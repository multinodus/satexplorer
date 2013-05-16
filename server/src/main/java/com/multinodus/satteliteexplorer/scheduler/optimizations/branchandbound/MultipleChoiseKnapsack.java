package com.multinodus.satteliteexplorer.scheduler.optimizations.branchandbound;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 17.05.13
 * Time: 0:12
 * To change this template use File | Settings | File Templates.
 */
public class MultipleChoiseKnapsack {

/* ======================================================================
	      MCKNAP.c, David Pisinger   oct 1993, modified july 1994
   ====================================================================== */

/* This is the C-code corresponding to the paper:
 *
 *   D. Pisinger
 *   A minimal algorithm for the multiple-choice knapsack problem
 *   European Journal of Operational Research, 83 (1995), 394-410
 *
 * Further details on the project can also be found in
 *
 *   D. Pisinger
 *   Algorithms for Knapsack Problems
 *   Report 95/1, DIKU, University of Copenhagen
 *   Universitetsparken 1
 *   DK-2100 Copenhagen
 *
 * The current code is intended for performing extensive tests with
 * randomly generated instances. It should however be easy to derive
 * the "plain" mcknap algorithm from the listing by stripping several
 * test routines.
 *
 * The code has been tested on a hp9000/735, and conforms with the
 * ANSI-C standard apart from some of the timing routines (which may
 * be removed). To compile the code use:
 *
 *   cc -Aa -O -o mcknap mcknap.c -lm
 *
 * The code is run by issuing the command
 *
 *   mcknap k n r type
 *
 * where k: number of classes,
 *       n: number of items in each class,
 *       r: range of coefficients,
 *       type: 1=uncorr., 2=weakly corr., 3=strongly corr., 4=subset sum
 *             5=zig-zag (sinha-zoltners), 6=0-1knapsack problem
 * output will be appended to the file "trace.mc".
 *
 * The code may only be used for academic or non-commercial purposes.
 * Errors and questions are refered to:
 *
 *   David Pisinger, associate professor
 *   DIKU, University of Copenhagen,
 *   Universitetsparken 1,
 *   DK-2100 Copenhagen.
 *   e-mail: pisinger@diku.dk
 *   fax: +45 35 32 14 01
 */


/* ======================================================================
                                  definitions
   ====================================================================== */

  private static int TRACELEVEL = 0;                /* level of debug information */
  private static int START = 1;                /* first test to be run */
  private static int TESTS = 100;              /* last test to be run */
  private static int SYNC = 5;   /* when to switch to linear scan in binary scan */
  private static int MEDIMAX = 15;
  private static int MAXSTACK = 100;
  private static int MAXLIST = 32;
  private static long MAXVTYPE = Long.MAX_VALUE;
  private static int TRUE = 1;
  private static int FALSE = 0;
  private static int MAXIMIZE = 0;
  private static int MINIMIZE = 1;

  private double DET(double a1, double a2, double b1, double b2){
    return a1 * b2 - a2 * b1;
  }

  /* partial vector */
  private class partvect{
    long     psum;
    long     wsum;
    long     vect;
  }

  /* item */
  private class itemrec {
    short    psum;
    short    wsum;
  }

  /* set of partial vectors */
  private class itemset {
    int    size;
    itemrec  fset;
    itemrec  lset;
    itemrec  no;
    itemrec  f;
    itemrec  l;
    int  used;
  }

  /* set of partial vectors */
  private class partset {
    int    size;
    partvect fset;
    partvect lset;
  }

  /* set of itemsets */
  private class isetset {
    itemset  fset;
    itemset  lset;
    int    size;
  }

  /* order record */
  private class ordrec {
    short     dp;
    short     dw;
    itemset  ref;
  }

  /* order interval */
  private class ordintv {
    ordrec   f;
    ordrec   l;
  }

  /* order stack */
  private class ordstack {
    ordintv  intv[MAXSTACK];
    int      level;
    int      optim;
    ordrec   first;
    ordrec   last;
    ordrec   i;
  }

  /* solution record */
  private class solrec {
    int    size;
    itemset  set;
  }

  /* solution structure */
  private class solstruct {
    solrec   list[MAXLIST];
    int    size;
    long    psum;
    long    wsum;
    long     vect;
    long     vmax;
    ordrec   a;
    ordrec   b;
  }

  private class allinfo { /* all problem information */
    int k;
    int n;
    int   type;
    short range;

    long capacity;	      /* capacity of knapsack */
    long dantzig;              /* the dantzig upper bound */
    long zstar;                /* optimal solution */
    long summul;	  	      /* sum of multiplications */
    long antmul;		      /* number of multiplications */
    long maxmul;               /* max multiplied set */
    long redusets;             /* sum of reduced sets */
    long reduitems;            /* sum of items which are tested for reduce */
    long redukill;             /* sum of tested items which were reduced */
    long gap;                  /* current gap */
    long partitions;           /* number of partitions */
    long domikill;             /* number of dominated-kills */
    long lpkill;               /* number of lp-kills */
    long  timepar;              /* time used for partitioning */
    long  timesort;             /* time used for sorting of gradients */
    long  time;                 /* time used for all solution */
    long  welldef;              /* is the found solution correct */
    long  checked;              /* optimal solution checked */
    long  iterates;             /* number of iterations to find optimal sol */
  }


/* ======================================================================
				  global variables
   ====================================================================== */

  solstruct solution;
  solstruct optsol;


/* ======================================================================
				  pushstack
   ====================================================================== */

  void pushstack(ordstack stack, ordrec f, ordrec l)
  {
    int v;

    (stack.level)++; v = stack.level;
    if (v == MAXSTACK) throw new Exception("stack filled");
    stack.intv[v].f = f;
    stack.intv[v].l = l;
  }


/* ======================================================================
				 checksolution
   ====================================================================== */

  void checksolution(allinfo a, isetset head, long zstar, long cstar)
  {
    itemset jm, j;
    long psum, wsum;

    psum = wsum = 0;
    jm = head.lset;
    for (j = head.fset; j <= jm; j++) {
      psum += j.f.psum;
      wsum += j.f.wsum;
    }
    a.checked = (psum == zstar) && (wsum <= cstar) && (zstar <= a.dantzig);
    if (!a.checked) {
      throw new Exception("WRONG SOLUTION");
    }
  }


/* ======================================================================
				  rotatesol
   ====================================================================== */

  void rotatesol(partset a, itemset b)
  {
    partvect j;
    partvect jm;
    int size, i;
    long vmax;

  /* check for sufficiency */
    size = b.size;
    while (MAXVTYPE / solution.vmax < size) {
      solution.vmax = solution.vmax / solution.list[solution.size-1].size;
      solution.size--;
    }
    vmax = solution.vmax;

  /* rotate array left */
    solution.size++;
    for (i = solution.size; i >= 1; i--) {
      solution.list[i] = solution.list[i-1];
    }

  /* place at end */
    solution.list[0].size = size;
    solution.list[0].set  = b;
    solution.vmax = vmax * size;

  /* now rotate partset */
    jm = a.lset;
    for (j = a.fset; j <= jm; j++) j.vect = (j.vect % vmax) * size;
  }


/* ======================================================================
				  savesol
   ====================================================================== */

  void savesol(partvect v, ordrec a, ordrec b)
  {
    optsol = solution;
    optsol.psum = v.psum;
    optsol.wsum = v.wsum;
    optsol.vect = v.vect;
    optsol.a    = a;
    optsol.b    = b;
  }


/* ======================================================================
				  definesol
   ====================================================================== */

  int definesol(allinfo al, long fixp, long fixw,
                    ordstack a, ordstack b, long c, long z, long ub)
  {
    long vect, rem;
    long psum, wsum;
    itemrec jc;
    ordrec i;
    solrec s;
    int k;

    vect = optsol.vect;
    psum = optsol.psum; wsum  = optsol.wsum;

  /* prepare sets for next iteration */
    for (i = a.first; i <= optsol.a; i++)
      if (i.ref != null) i.ref.used = FALSE;
    for (i = optsol.b; i <= b.last;  i++)
      if (i.ref != null) i.ref.used = FALSE;

  /* find solution vector */
    for (k = 0; k < optsol.size; k++) {
      s = &(optsol.list[k]);
      rem  = vect  % s.size;
      jc = s.set.fset + rem;

      psum -= jc.psum - s.set.f.psum;
      wsum -= jc.wsum - s.set.f.wsum;
      s.set.f    = jc;  /* save choice in f */
      s.set.used = TRUE; /* avoid recalculation */
      vect  = vect  / s.size;
    }

    al.welldef = (fixp == psum) && (fixw == wsum);
    if (al.welldef) return TRUE;

  /* new problem */
    z = psum - 1; c = wsum; ub = psum;
    a.i = a.first; b.i = b.last;
    return FALSE;
  }


/* ======================================================================
				  findvect
   ====================================================================== */

  partvect findvect(long ws, partvect f, partvect l)
  {
  /* find vector i, so that i.wsum <= ws < (i+1).wsum */
    partvect m;

  /* a set should always have at least one vector */
    if (f > l) throw new Exception("findvect: empty set");

    if (f.wsum >  ws) throw new Exception("findvect: too big");
    if (l.wsum <= ws) return l;

    while (l - f > SYNC) {
      m = f + (l - f) / 2;
      if (m.wsum > ws) {
        l = m-1;
      } else {
        f = m;
      }
    }
    while (l.wsum > ws) l--;

    if (l.wsum     >  ws) throw new Exception("findvect: too big l");
    if ((l+1).wsum <= ws) throw new Exception("findvect: too small l");
    return l;
  }


/* ======================================================================
				  maketest
   ====================================================================== */

  int icmp(short a, short b) { return a - b; }

  void maketest(itemset j, int r, int type)
  {
    itemrec i;
    itemrec im;
    short p, w;
    long ps, ws;
    int k, n;

    n = j.size;
    if (type == 3) r = (2*r) / n;
    if ((type == 3) || (type == 5)) {
      p = palloc(n * (long) sizeof(short));
      w = palloc(n * (long) sizeof(short));
      for (k = 0; k < n; k++) {
        w[k] = random(r) + 1;
        p[k] = (type == 3 ? w[k] + 10 : random(r) + 1);
      }
      qsort(p, n, sizeof(short), (funcptr) icmp);
      qsort(w, n, sizeof(short), (funcptr) icmp);
    }

    im = j.lset; ps = 0; ws = 0;
    for (i = j.fset; i <= im; i++) {
      switch (type) {
        case  1: i.wsum = random(r) + 1;
          i.psum = random(r) + 1;
          break;
        case  2: i.wsum = random(r) + 1;
          i.psum = random(21) + i.wsum - 10;
          if (i.psum <= 0) i.psum = 1;
          break;
        case  3: i.wsum = w[i - j.fset] + ws;
          i.psum = p[i - j.fset] + ps;
          break;
        case  4: i.wsum = random(r) + 1;
          i.psum = i.wsum;
          break;
        case  5: i.wsum = w[i - j.fset];
          i.psum = p[i - j.fset];
          break;
        case  6: if (i == j.fset) { i.wsum = 0; i.psum = 0; break; }
          i.wsum = random(r) + 1;
          i.psum = random(r) + 1;
          break;
        default: i.wsum = 0;
          i.psum = 0;
          break;
      }
      ws = i.wsum; ps = i.psum;
    }

    if ((type == 3) || (type == 5)) {
      pfree(p);
      pfree(w);
    }
  }


/* ======================================================================
				 inititems
   ====================================================================== */

  long inititems(allinfo al, isetset h,
                  int classes, int size, int r, int type)
  {
    itemset j, jm, k;
    itemrec i, im, mi, ma;
    long wsum1, wsum2;

  /* init itemset */
    h.size  = classes;
    h.fset  = palloc(h.size * (long) sizeof(itemset));
    h.lset  = h.fset + h.size - 1;

  /* generate test classes */
    jm = h.lset;
    for (j = h.fset; j <= jm; j++) {
      j.size = size;
      j.fset = palloc(size * (long) sizeof(itemrec));
      j.lset = j.fset + size - 1;
      maketest(j, r, type);
    }

  /* find c as 1/2 of extreme weights in each set */
    wsum1 = 0; wsum2 = 0;
    for (j = h.fset; j <= jm; j++) {
      im = j.lset;
      mi = im; ma = im;
      for (i = j.fset; i < im; i++) {
        if (i.wsum < mi.wsum) mi = i;
        if (i.psum >= ma.psum) {
          if ((i.psum > ma.psum) || (i.wsum < ma.wsum)) ma = i;
        }
      }
      wsum1 += mi.wsum;
      wsum2 += ma.wsum;
    }
    al.capacity = (wsum1 + wsum2) / 2;
    vis(2,"SETS %hd, MINW %ld, MAXW %ld, C %ld\n",
        classes, wsum1, wsum2, al.capacity);
    return al.capacity;
  }


/* ======================================================================
				    merge
   ====================================================================== */

  void merge(partset jset, itemset  iset, int f, int l,
             partvect k1, partvect km)
  {
    if (f == l) {
      partvect j, k;
      short psum, wsum;
      partvect j1, jm;
      itemrec i;
      int d;

      d  = jset.size;
      j1 = jset.fset; jm = jset.lset;
      k1 = palloc((d+1) * (long) sizeof(partvect));/* 1 extra is used below */
      km = k1 + d - 1;
      i = iset.fset + f;         /* add item i minus lp-choice in set iset */
      psum = i.psum - iset.f.psum;
      wsum = i.wsum - iset.f.wsum;
      for (k = k1, j = j1; j <= jm; k++, j++) {
        k.psum = j.psum + psum;
        k.wsum = j.wsum + wsum;
        k.vect = j.vect + f;
      }
    } else {
      partvect k, a, b;
      partvect a1, am, b1, bm;
      long size;
      int d;

      d = (l - f) / 2;
      merge(jset, iset, f,     f+d, &a1, &am);
      merge(jset, iset, f+d+1, l,   &b1, &bm);

      size = (am - a1 + 1) + (long) (bm - b1 + 1) + 1; /* 1 extra used below */
      k1 = palloc(size * (long) sizeof(partvect));
      a = a1; b = b1; k = k1;
      if (a.wsum <= b.wsum) { k = a; a++; } else { k = b; b++; }
      (am+1).wsum = bm.wsum + 1; (am+1).psum = 0;/* add max as extra item */
      (bm+1).wsum = am.wsum + 1; (bm+1).psum = 0;
      am++; bm++;

      for (;;) {
        if (a.wsum <= b.wsum) {
          if (a.psum > k.psum) {
            if (a.wsum > k.wsum) k++;
            k = a;
          } a++;
        } else {
          if (b.psum > k.psum) {
            if (b.wsum > k.wsum) k++;
            k = b;
          } b++;
        }
        if ((a == am) && (b == bm)) break;
      }
      km = k;
      pfree(a1);
      pfree(b1);
    }
  }


/* ======================================================================
				  multiply
   ====================================================================== */

  void multiply(allinfo al, partset a, itemset b)
  {
    partvect k1, km;
    int size;
    long vmax;

    rotatesol(a, b);
    merge(a, b, 0, b.size-1, k1, km);
    pfree(a.fset);
    a.fset = k1;
    a.lset = km;
    size = SIZE(a);
    vis(2,"MULTIPLY (%ld*%ld) = %ld . %ld\n",
        (long) a.size, (long) b.size, a.size * (long) b.size, (long) size);
    a.size = size;
    if (size > al.maxmul) al.maxmul = size;
  }


/* ======================================================================
				  reduceset
   ====================================================================== */

  void reduceset(partset a, ordrec t, ordrec s, long z1, long c)
  {
    partvect i, k;
    short ps, ws, pt, wt;
    long z;
    partvect r1, rm, v;

    if (a.size == 0) return;
    pt = t.dp; wt = t.dw;
    ps = s.dp; ws = s.dw;

  /* initialize limits */
    r1 = a.fset;
    rm = a.lset;

    if (r1.wsum > c) {
    /* alle overvaegtige */
      v = r1 - 1;
    } else {
      v = findvect(c, r1, rm);
      if (v.psum > z1) {
        savesol(v, t, s);
        z1 = v.psum;
      }
    }
    z = z1 + 1;

  /* now do the reduction */
    k = r1;
    for (i = r1; i != v+1; i++) {
      if (DET(i.psum-z, i.wsum-c, pt, wt) >= 0) { k = i; k++; }
    }
    for (i = v+1; i <= rm; i++) {
      if (DET(i.psum-z, i.wsum-c, ps, ws) >= 0) { k = i; k++; }
    }
    vis(2,"Z=%ld, reduceset %3d . %3d  s(%hd,%hd) t(%hd,%hd)\n",
        z1, (int) a.size, (int) (k - a.fset), ps, ws, pt, wt);
    a.lset = k - 1;
    a.size = SIZE(a);
  }


/* ======================================================================
				  reduceitem
   ====================================================================== */

  void reduceitem(allinfo al, itemset a, long psum, long wsum,
                  short pb, short wb, long z1, long c)
  {
    itemrec i;
    long z, ub, psum1, wsum1;
    itemrec i1, im;

    if (a.size == 1) return;

  /* sum of fixed items in greedy solution */
    psum -= a.f.psum;
    wsum -= a.f.wsum;

    al.redusets++;
  /* now do the reduction */
    i1 = a.fset; im = a.lset; z = z1 + 1;
    for (i = i1; i <= im; ) {
      al.reduitems++;
      psum1 = i.psum + psum; wsum1 = i.wsum + wsum;
      if (DET(psum1-z,wsum1-c,pb,wb) >= 0) { i++; }
      else { SWAPI(i,im); im--; al.redukill++; }
    }
    vis(2,"Z=%ld, reitem %3d . %3d\n",
        z1, (int) a.size, (int) (i - a.fset));
    a.lset = i - 1;
    a.size = SIZE(a);
  }


/* ======================================================================
				  preprocess
   ====================================================================== */

  void preprocess(isetset head, long fixp, long fixw,
                  long minw1, long maxw1)
  {
    itemrec i;
    itemrec im, f, l;
    itemset j, jm;
    long minw, maxw, lw;
    long kill, setout;

    vis(2,"\nPreprocess sets %hd ", head.size);
    minw = maxw = 0; kill = setout = 0;
    jm = head.lset;
    for (j = head.fset; j <= jm; ) {
      im = j.lset;
      f = l = im;
      for (i = j.fset; i < im; i++) {
        if (i.wsum <  f.wsum) f = i;
        if (i.psum >= l.psum) {
          if ((i.psum > l.psum) || (i.wsum < l.wsum)) l = i;
        }
      }
      j.f = f; minw += f.wsum;
      j.l = l; maxw += l.wsum;

    /* now remove dominated */
      lw = l.wsum; SWAPI(j.fset,l);
      for (i = j.fset+1; i <= im; ) {
        if (i.wsum >= lw) { SWAPI(i, im); im--; kill++; } else {	i++; }
      }
      j.lset = im;
      if (j.fset == j.lset) {
        fixp += im.psum;
        fixw += im.wsum;
        SWAPS(j, jm); jm--;
        setout++;
      } else {
        j++;
      }
    }
    head.lset = jm;
    vis(2,"now %hd fix (%ld,%ld) kill %hd\n", head.size, fixp, fixw, kill);
    minw1 = minw; maxw1 = maxw;
  }


/* ======================================================================
				  choosemedian
   ====================================================================== */

  int lamless(ordrec a, ordrec b)
  {
    long sum;
    sum = DET(a.dp, a.dw, b.dp, b.dw);
    if (sum < 0) return -1;
    return (sum > 0);
  }


  void choosemedian(isetset head, short cdp, short cdw)
  {
    int d;
    short dp, dw;
    itemset i;
    ordrec a[MEDIMAX];

    for (d = 0; d < MEDIMAX; d++) {
      i = head.fset + random(SIZE(head)); /* random choice */
      a[d].dp = i.l.psum - i.f.psum;
      a[d].dw = i.l.wsum - i.f.wsum;
    }

    qsort(&a, d, sizeof(ordrec), (funcptr) lamless);
    cdp = a[d/2].dp; cdw = a[d/2].dw;
    vis(2,"\nmedian (%hd,%hd)\n", cdp, cdw);
  }


/* ======================================================================
				  outermost
   ====================================================================== */

  void outermost(isetset head, short dp, short dw,
                 long minwsum, long maxwsum)
  {
    itemrec i, no;
    itemrec i1, im;
    itemset j, jm;
    long sum, msum;

    jm = head.lset;
    for (j = head.fset; j <= jm; j++) {

    /* find outermost item */
      i1 = j.fset; im = j.lset; msum = LONG_MIN;
      for (i = i1; i <= im; i++) {
        sum = DET(i.psum, i.wsum, dp, dw);
        if (sum >= msum) {
          if (sum > msum) { msum = sum; no = i1; }
          SWAPI(no, i); no++;
        }
      }
      no--;

    /* determine min and max weightsums */
      if (no != i1) {
        if (i1.wsum > no.wsum) SWAPI(i1, no);
        for (i = i1+1; i < no; i++) {
          if (i.wsum < i1.wsum) { SWAPI(i, i1); } else
          if (i.wsum > no.wsum) { SWAPI(i, no); }
        } /* now i1.wsum minimal, no.wsum maximal */
      }
      minwsum += i1.wsum;
      maxwsum += no.wsum;
      j.no = no;
    }
  }


/* ======================================================================
				  separete
   ====================================================================== */

  void separate(allinfo al, isetset head, int underfull,
                long fixp, long fixw)
  {
    itemrec i, im;
    itemrec i1;
    itemset j, jm;
    short p1, w1, pm, wm;
    long dkill, left, setout;

    dkill = left = setout = 0;
    jm = head.lset;
    for (j = head.fset; j <= jm; ) {

    /* choose min and max items for partitioning */
      i1 = j.fset; im = j.lset;
      if (underfull) { SWAPI(j.no, i1); j.f = i1; } else { j.l = i1; }
      p1 = j.f.psum; wm = j.l.wsum;

    /* delete dominated or too big/small items */
      if (underfull) {
        for (i = i1+1; i <= im; ) {
          if (i.psum <= p1) { SWAPI(i, im); im--; dkill++; }
          else { i++; left++; }
        }
      } else {
        for (i = i1+1; i <= im; ) {
          if (i.wsum >= wm) { SWAPI(i, im); im--; dkill++; }
          else { i++; left++; }
        }
      }
      j.lset = im;

    /* check for singleton sets */
      if (j.fset == j.lset) {
        fixp += j.fset.psum;
        fixw += j.fset.wsum;
        SWAPS(j, jm); jm--;
        setout++;
      } else {
        j++;
      }
    }
    head.lset    = jm;
    al.domikill += dkill;
    al.lpkill   += left;
    if (dkill == 0) error("partitioning with no domi-effect");
  }


/* ======================================================================
				  optimum
   ====================================================================== */

  void optimum(isetset head, long fixp, long fixw, long c)
  {
    itemrec i;
    long ps, ws;
    itemrec i1, im, choice;
    itemset j, jm, cut;

  /* define solution: first choose smallest items */
    jm = head.lset;
    for (j = head.fset; j <= jm; j++) {
      i1 = j.fset; fixp += i1.psum; fixw += i1.wsum;
    }

  /* then improve till filled */
    cut = null;
    for (j = head.fset; j <= jm; j++) {
      if ((fixw == c) && (cut != null)) break;
      i1 = j.fset; im = j.no;
      for (i = i1+1; i <= im; i++) {
        ps = fixp + i.psum - i1.psum;
        ws = fixw + i.wsum - i1.wsum;
        if (ws <= c) {
          if (ps > fixp) { fixp = ps; fixw = ws; SWAPI(i1, i); }
        } else {
          cut = j;
        }
      }
    }

  /* a set containing fractional variables is placed first */
    if (cut != null) SWAPS(head.fset, cut);
  }


/* ======================================================================
				  partition
   ====================================================================== */

  void partition(allinfo al, isetset head, long c,
                 long psum, long wsum, short dp, short dw)
  {
    long fixp, fixw, minwsum, maxwsum;

    fixp = 0; fixw = 0; al.partitions = 0;

  /* check for trivial solutions and reduce trivially dominated */
    preprocess(head, fixp, fixw, minwsum, maxwsum);
    if ((minwsum > c) || (maxwsum <= c)) return;

    for (;;) {
      al.partitions++;
      choosemedian(head, dp, dw);

    /* find projections in direction (dp,dw) */
      minwsum = fixw; maxwsum = fixw;
      outermost(head, dp, dw, minwsum, maxwsum);

    /* now consider the weight sums */
      if ((minwsum <= c) && (c <= maxwsum)) break;

    /* separete set in dominated and live items */
      separate(al, head, (maxwsum < c), fixp, fixw);
    }
  /* now find the optimal lp-solution */
    optimum(head, fixp, fixw, c);

    psum = fixp; wsum = fixw;
    al.dantzig = fixp + ((c - fixw) * dp) / dw;
  }


/* ======================================================================
				  restore
   ====================================================================== */

  void restore(isetset head, long psum, long wsum)
  {
    itemset j, jm;

    head.lset = head.fset + head.size - 1;
    jm = head.lset;
    for (j = head.fset; j <= jm; j++) {
      j.lset = j.fset + j.size - 1;
      j.used = FALSE;
      j.f    = (j.fset);
      psum -= j.f.psum;
      wsum -= j.f.wsum;
    }
    if ((psum != 0) || (wsum != 0)) throw new Exception("choices not first");
  }


/* ======================================================================
				 domiitem
   ====================================================================== */

  int itemless(itemrec a, itemrec b)
  {
    short sum;
    sum = a.wsum - b.wsum;
    if (sum != 0) return sum;
    return (a.psum - b.psum);
  }


  void domiitem(itemset mid)
  {
    itemrec i, j, k;
    itemrec k1, im, i1;

    i1 = mid.fset; im = mid.lset;
    if (i1 == im) return;

    qsort(mid.fset, SIZE(mid), sizeof(itemrec), (funcptr) itemless);

  /* now remove dominated */
    k1 = palloc(mid.size * (long) sizeof(itemrec));
    for (i = i1+1, j = i1, k = k1; i <= im; i++ ) {
      if (i.psum > j.psum) { j++; j = i; } else { k = i; k++; }
    }
    mid.lset = j;
    mid.size = SIZE(mid);

  /* copy dominated to end of set */
    for (i = k1; i < k; i++) { j++; j = i; }
    pfree(k1);
  }


/* ======================================================================
				 initfirst
   ====================================================================== */

  void initfirst(partset mid, itemset old, long psum, long wsum)
  {
    itemrec i, i1, im;
    partvect j;

    domiitem(old);
    mid.fset = palloc(old.size * (long) sizeof(partvect));
    i1 = old.fset; im = old.lset;
    psum -= old.f.psum; wsum -= old.f.wsum; /* subtract lp-choice */
    for (i = i1, j = mid.fset; i <= im; i++) {
      j.psum = i.psum + psum;
      j.wsum = i.wsum + wsum;
      j.vect = (i - i1); /* number of choice */
      j++;
    }
    mid.lset = j - 1;
    mid.size = SIZE(mid);
    solution.size = 1;
    solution.vmax = mid.size;
    solution.list[0].size = old.size;
    solution.list[0].set  = old;
  }


/* ======================================================================
				partsort
   ====================================================================== */

  void partsort(ordstack stack, ordrec f, ordrec l)
  {
    short mp, mw;
    ordrec i, j, m;
    int d;

    d = l - f + 1;
    if (d <= 1) return;
    m = f + d / 2;
    if (DET(f.dp, f.dw, m.dp, m.dw) < 0) SWAPO(f, m);
    if (d > 2) {
      if (DET(m.dp, m.dw, l.dp, l.dw) < 0) {
        SWAPO(m, l);
        if (DET(f.dp, f.dw, m.dp, m.dw) < 0) SWAPO(f, m);
      }
    }
    if (d <= 3) return;

    mp = m.dp; mw = m.dw; i = f; j = l;
    for (;;) {
      do i++; while (DET(i.dp, i.dw, mp, mw) > 0);
      do j--; while (DET(j.dp, j.dw, mp, mw) < 0);
      if (i > j) break;
      SWAPO(i, j);
    }

    if (stack.optim == MINIMIZE) {
      pushstack(stack, f, i-1); partsort(stack, i, l);
    } else {
      pushstack(stack, i, l); partsort(stack, f, i-1);
    }
  }


/* ======================================================================
			       checkinterval
   ====================================================================== */

  void checkinterval(ordstack s)
  {
    int l;
    ordintv top;

    if (s.level == -1) return; /* nothing to pop */
    top = &(s.intv[s.level]);
    if ((top.f <= s.i) && (s.i <= top.l)) {
    /* current i is in next interval */
      (s.level)--;
      partsort(s, top.f, top.l);
    }
  }


/* ======================================================================
				 defineedges
   ====================================================================== */

  void defineedges(ordstack stacka, ordstack stackb, isetset head)
  {
    itemrec i;
    short p1, w1;
    itemrec i1, im, m1, m2, d1, d2;
    itemset j, jm;
    ordrec a, b;

    jm = head.lset; a = stacka.first; b = stackb.last;
    for (j = head.fset+1; j <= jm; j++, a++, b--) {
      i1 = j.fset;   im = j.lset;
      p1 = j.f.psum; w1 = j.f.wsum;
      d1.psum = p1; d1.wsum = w1+1; m1 = d1;
      d2.psum = p1-1; d2.wsum = w1; m2 = d2;
      for (i = i1+1; i <= im; i++) {
        if (i.wsum > w1) {
          if (DET(i.psum-p1,i.wsum-w1,m1.psum-p1,m1.wsum-w1) > 0) m1 = i;
        }
        if (i.wsum < w1) {
          if (DET(p1-i.psum,w1-i.wsum,p1-m2.psum,w1-m2.wsum) < 0) m2 = i;
        }
      }
      a.dp = m1.psum - p1; a.dw = m1.wsum - w1; a.ref = j;
      b.dp = p1 - m2.psum; b.dw = w1 - m2.wsum; b.ref = j;
    }
    a.dp = 0; a.dw = 1; a.ref= null;
    b.dp = 1; b.dw = 0; b.ref= null;

    partsort(stacka, stacka.first, stacka.last-1);
    partsort(stackb, stackb.first+1, stackb.last);
  }


/* ======================================================================
				 makestacks
   ====================================================================== */

  void makestacks(ordstack stacka, ordstack stackb, isetset head)
  {
    stacka.first = palloc(head.size * (long) sizeof(ordrec));
    stackb.first = palloc(head.size * (long) sizeof(ordrec));
    stacka.last  = stacka.first + head.size - 1;
    stackb.last  = stackb.first + head.size - 1;
    stacka.level = -1;
    stackb.level = -1;
    stacka.optim = MAXIMIZE;
    stackb.optim = MINIMIZE;
    stacka.i     = stacka.first;
    stackb.i     = stackb.last;
  }

/* ======================================================================
				 minmcknap
   ====================================================================== */

  void minmcknap(int k, int n, short r, int type)
  {
    allinfo a;
    ordstack stacka, stackb;
    long cstar, psum, wsum, z, c, ub;
    partset mid;
    isetset head;
    itemset s;
    short pb, wb;
    int optimal;
    int i;

    a.k        = k;
    a.n        = n;
    a.range    = r;
    a.type     = type;

  /* make test example */
    cstar = inititems(a, head, k, n, r, type);

    a.summul   = 0;
    a.maxmul   = 0;
    a.antmul   = 0;
    a.redusets = 0;
    a.reduitems= 0;
    a.redukill = 0;
    a.domikill = 0;

    c = cstar;
    makestacks(stacka, stackb, head);
    partition(a, head, c, psum, wsum, pb, wb);
    restore(head, psum, wsum);

    if (psum == a.dantzig) {
      stacka.i.dp = pb; stacka.i.dw = wb; stacka.i.ref = head.fset;
      stackb.i.dp = pb; stackb.i.dw = wb; stackb.i.ref = head.fset;
    } else {
      defineedges(stacka, stackb, head);
    }

    z = psum-1; ub = a.dantzig;
    for (i = 1; ; i++) {
      initfirst(mid, head.fset, psum, wsum);
      for (;;) {
        reduceset(mid, stacka.i, stackb.i, z, c);
        if ((mid.size == 0) || (z == ub)) break;

        s = stacka.i.ref; if (s == null) break;
        (stacka.i)++; checkinterval(stacka);
        if (!s.used) {
          reduceitem(a, s, psum, wsum, pb, wb, z, c);
          domiitem(s);
          if (s.size > 1) {
            multiply(a, mid, s);
            a.antmul++;
            a.summul += mid.size;
            if (mid.size > a.maxmul) a.maxmul = mid.size;
          }
          s.used = TRUE;
        }

        reduceset(mid, stacka.i, stackb.i, z, c);
        if ((mid.size == 0) || (z == ub)) break;

        s = stackb.i.ref; if (s == null) break;
        (stackb.i)--; checkinterval(stackb);
        if (!s.used) {
          reduceitem(a, s, psum, wsum, pb, wb, z, c);
          domiitem(s);
          if (s.size > 1) {
            multiply(a, mid, s);
            a.antmul++;
            a.summul += mid.size;
            if (mid.size > a.maxmul) a.maxmul = mid.size;
          }
          s.used = TRUE;
        }
      }
      if (i == 1) a.zstar = z;
      optimal = definesol(&a, psum, wsum, stacka, stackb, c, z, ub);

      if (optimal) { a.iterates += i; break; }
      if (i > 10) new Exception("for mange runder");
    }

    a.gap = a.dantzig - a.zstar;

    checksolution(&a, &head, a.zstar, cstar);
  }


/* ======================================================================
				    main
   ====================================================================== /
*/
  void main(int argc, char argv[])
  {
    int n, r, k, type, v;
    long c;
    isetset head;

    for (v = START; v <= TESTS; v++) {
      minmcknap(k, n, r, type);
    }
  }
}
