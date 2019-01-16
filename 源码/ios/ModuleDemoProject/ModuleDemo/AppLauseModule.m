//
//  UZModuleDemo.m
//  UZModule
//
//  Created by kenny on 14-3-5.
//  Copyright (c) 2014年 APICloud. All rights reserved.
//

#import "AppLauseModule.h"
#import "UZAppDelegate.h"
#import "NSDictionaryUtils.h"

@interface AppLauseModule ()
{
    NSInteger _cbId;
}

@property (strong, nonatomic) UIButton *applauseBtn;
@property (strong, nonatomic) UILabel *applauseNumLbl;
@property (assign, nonatomic) NSInteger applauseNum;
@property (nonatomic) CGRect defaultPlayRect;//全局默认视频大小控制
@property (nonatomic, copy) NSArray *iamges;
@end

@implementation AppLauseModule

+ (void)launch {
    //在module.json里面配置的launchClassMethod，必须为类方法，引擎会在应用启动时调用配置的方法，模块可以在其中做一些初始化操作
}

- (id)initWithUZWebView:(UZWebView *)webView_ {
    if (self = [super initWithUZWebView:webView_]) {
        
    }
    return self;
}

- (void)dispose {
    //do clean
}

- (void)showAppLause:(NSDictionary *)paramDict {
    _cbId = [paramDict integerValueForKey:@"cbId" defaultValue:-1];
    
    NSDictionary *rect = [paramDict dictValueForKey:@"rect" defaultValue:nil];
    if(rect==nil){
        self.defaultPlayRect = CGRectMake(0, 0, 60, 60);
    }else{
        float x = [rect floatValueForKey:@"x" defaultValue:0];
        float y = [rect floatValueForKey:@"y" defaultValue:0];
        float w = [rect floatValueForKey:@"w" defaultValue:60];
        if(w<=0){
            w = 60;
        }
        float h = [rect floatValueForKey:@"h" defaultValue:60];
        
        if(h<=0){
            h= 60;
        }
        self.defaultPlayRect = CGRectMake(x, y, w, h);
    }
    self.applauseNum = [paramDict integerValueForKey:@"applauseNum" defaultValue:0];
    BOOL isShowNum = [paramDict boolValueForKey:@"isShowNum" defaultValue:NO];
    
    NSString *image1 = [paramDict stringValueForKey:@"placeholderImg" defaultValue:nil];
    self.iamges = [paramDict arrayValueForKey:@"iamges" defaultValue:nil];
    
    if(image1.length<=0){
        NSDictionary *ret = [NSDictionary dictionaryWithObject:[NSNumber numberWithBool:NO] forKey:@"status"];
        NSDictionary *err = [NSDictionary dictionaryWithObject:@"显示图片不能为空!" forKey:@"msg"];
        [self sendResultEventWithCallbackId:_cbId dataDict:ret errDict:err doDelete:false];
        return;
    }
    
    NSString *placeholderImg = [self getPathWithUZSchemeURL:image1];
    NSString *mFixedOn = [paramDict stringValueForKey:@"fixedOn" defaultValue:nil];
    NSLog(@"%@",mFixedOn);
    BOOL mFixed = [paramDict boolValueForKey:@"fixed" defaultValue:NO];
    
    UIImage *homeImage = nil;
    if([placeholderImg hasPrefix:@"http"]){
        NSURL *url = [NSURL URLWithString: placeholderImg];
        homeImage = [UIImage imageWithData:[NSData dataWithContentsOfURL:url]];
    }else{
        homeImage = [UIImage imageNamed:placeholderImg];
    }
    if(homeImage==nil){
        homeImage = [UIImage imageNamed:@"applause"];
    }
    
    //鼓掌按钮
    self.applauseBtn = [[UIButton alloc]initWithFrame:self.defaultPlayRect];
    self.applauseBtn.contentMode = UIViewContentModeScaleToFill;
    [self.applauseBtn setImage:homeImage forState:UIControlStateNormal];
    [self.applauseBtn setImage:homeImage forState:UIControlStateHighlighted];
    [self.applauseBtn addTarget:self action:@selector(applauseBtnClick) forControlEvents:UIControlEventTouchUpInside];
    
    [self addSubview:self.applauseBtn fixedOn:mFixedOn fixed:mFixed];

    if(isShowNum){
        //鼓掌数
        self.applauseNumLbl = [[UILabel alloc]init];
        self.applauseNumLbl.textColor = [UIColor blackColor];
        self.applauseNumLbl.font = [UIFont systemFontOfSize:12];
        self.applauseNumLbl.text = [NSString stringWithFormat:@"%zd",self.applauseNum];
        [self.applauseBtn addSubview:self.applauseNumLbl];
        self.applauseNumLbl.textAlignment = NSTextAlignmentCenter;
        self.applauseNumLbl.frame = CGRectMake(6, 43, 50, 12);
    }

    NSMutableDictionary *ret = [NSMutableDictionary dictionaryWithObject:@"show" forKey:@"eventType"];
    [ret setObject:[NSNumber numberWithInteger:self.applauseNum] forKey:@"applauseNum"];
    [self sendResultEventWithCallbackId:_cbId dataDict:ret errDict:nil doDelete:NO];
}


- (void)clickAppLause:(NSDictionary *)paramDict {
     NSInteger cbId = [paramDict integerValueForKey:@"cbId" defaultValue:-1];
    if(self.applauseBtn==nil){
        NSDictionary *ret = [NSDictionary dictionaryWithObject:[NSNumber numberWithBool:NO] forKey:@"status"];
        NSDictionary *err = [NSDictionary dictionaryWithObject:@"请先执行显示按钮接口!" forKey:@"msg"];
        [self sendResultEventWithCallbackId:cbId dataDict:ret errDict:err doDelete:false];
        return;
    }
    
    if(self.applauseNum>999){
        self.applauseNum++;
        if(self.applauseNumLbl!=nil){
            self.applauseNumLbl.text = @"999+";
        }
    }else{
        self.applauseNum++;
        if(self.applauseNumLbl!=nil){
            self.applauseNumLbl.text = [NSString stringWithFormat:@"%zd",self.applauseNum];
        }
    }
    
    [self showTheApplauseInView:self.viewController.view belowView:self.applauseBtn];
    
    NSMutableDictionary *ret = [NSMutableDictionary dictionaryWithObject:@"click" forKey:@"eventType"];
    [ret setObject:[NSNumber numberWithInteger:self.applauseNum] forKey:@"applauseNum"];
    
    [self sendResultEventWithCallbackId:cbId dataDict:ret errDict:nil doDelete:NO];
}

- (void)applauseBtnClick {
    
    if(self.applauseNum>999){
        self.applauseNum++;
        if(self.applauseNumLbl!=nil){
            self.applauseNumLbl.text = @"999+";
        }
    }else{
        self.applauseNum++;
        if(self.applauseNumLbl!=nil){
            self.applauseNumLbl.text = [NSString stringWithFormat:@"%zd",self.applauseNum];
        }
    }
    
    
    
    [self showTheApplauseInView:self.viewController.view belowView:self.applauseBtn];
    
    NSMutableDictionary *ret = [NSMutableDictionary dictionaryWithObject:@"click" forKey:@"eventType"];
    [ret setObject:[NSNumber numberWithInteger:self.applauseNum] forKey:@"applauseNum"];
    
    [self sendResultEventWithCallbackId:_cbId dataDict:ret errDict:nil doDelete:NO];
}
//鼓掌动画
- (void)showTheApplauseInView:(UIView *)view belowView:(UIButton *)v{
    UIImage *imagePic = nil;
    
    if(self.iamges==nil){
        NSInteger index = arc4random_uniform(7); //取随机图片
        NSString *image = [NSString stringWithFormat:@"applause_%zd",index];
        imagePic = [UIImage imageNamed:image];
    }else{
        NSInteger count =[self.iamges count];
        NSInteger index = arc4random_uniform((UInt32)count);
        NSString *temp =[self.iamges objectAtIndex:index];
        NSString *image = [self getPathWithUZSchemeURL:temp];
        
        if([image hasPrefix:@"http"]){
            NSURL *url = [NSURL URLWithString: image];
            imagePic = [UIImage imageWithData:[NSData dataWithContentsOfURL:url]];
        }else{
            imagePic = [UIImage imageNamed:image];
        }
        
        if(imagePic==nil){
            NSInteger index = arc4random_uniform(7);
            NSString *image = [NSString stringWithFormat:@"applause_%zd",index];
            imagePic = [UIImage imageNamed:image];
        }
        
    }
    UIWindow * window=[[[UIApplication sharedApplication] delegate] window];
    CGRect rect=[self.applauseBtn convertRect: self.applauseBtn.bounds toView:window];
    
    UIImageView *applauseView = [[UIImageView alloc]initWithFrame:CGRectMake(CGRectGetMinX(rect), CGRectGetMinY(rect)-CGRectGetHeight([self.applauseBtn frame])/2, 40, 40)];//增大y值可隐藏弹出动画
    [view insertSubview:applauseView belowSubview:v];
    applauseView.image = imagePic;
    
    CGFloat AnimH = CGRectGetMinY([self.applauseBtn frame]); //动画路径高度,
    applauseView.transform = CGAffineTransformMakeScale(0, 0);
    applauseView.alpha = 0;
    
    //弹出动画
    [UIView animateWithDuration:0.2 delay:0.0 usingSpringWithDamping:0.6 initialSpringVelocity:0.8 options:UIViewAnimationOptionCurveEaseOut animations:^{
        applauseView.transform = CGAffineTransformIdentity;
        applauseView.alpha = 0.9;
    } completion:NULL];
    
    //随机偏转角度
    NSInteger i = arc4random_uniform(2);
    NSInteger rotationDirection = 1- (2*i);// -1 OR 1,随机方向
    NSInteger rotationFraction = arc4random_uniform(10); //随机角度
    //图片在上升过程中旋转
    [UIView animateWithDuration:4 animations:^{
        applauseView.transform = CGAffineTransformMakeRotation(rotationDirection * M_PI/(4 + rotationFraction*0.2));
    } completion:NULL];
    
    //动画路径
    UIBezierPath *heartTravelPath = [UIBezierPath bezierPath];
    [heartTravelPath moveToPoint:applauseView.center];
    
    //随机终点
    CGFloat ViewX = applauseView.center.x;
    CGFloat ViewY = applauseView.center.y;
    CGPoint endPoint = CGPointMake(ViewX + rotationDirection*10, ViewY - AnimH);
    
    //随机control点
    NSInteger j = arc4random_uniform(2);
    NSInteger travelDirection = 1- (2*j);//随机放向 -1 OR 1
    
    NSInteger m1 = ViewX + travelDirection*(arc4random_uniform(20) + 50);
    NSInteger n1 = ViewY - 60 + travelDirection*arc4random_uniform(20);
    NSInteger m2 = ViewX - travelDirection*(arc4random_uniform(20) + 50);
    NSInteger n2 = ViewY - 90 + travelDirection*arc4random_uniform(20);
    CGPoint controlPoint1 = CGPointMake(m1, n1);//control根据自己动画想要的效果做灵活的调整
    CGPoint controlPoint2 = CGPointMake(m2, n2);
    //根据贝塞尔曲线添加动画
    [heartTravelPath addCurveToPoint:endPoint controlPoint1:controlPoint1 controlPoint2:controlPoint2];
    
    //关键帧动画,实现整体图片位移
    CAKeyframeAnimation *keyFrameAnimation = [CAKeyframeAnimation animationWithKeyPath:@"position"];
    keyFrameAnimation.path = heartTravelPath.CGPath;
    keyFrameAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionDefault];
    keyFrameAnimation.duration = 3 ;//往上飘动画时长,可控制速度
    [applauseView.layer addAnimation:keyFrameAnimation forKey:@"positionOnPath"];
    
    //消失动画
    [UIView animateWithDuration:3 animations:^{
        applauseView.alpha = 0.0;
    } completion:^(BOOL finished) {
        [applauseView removeFromSuperview];
    }];
}

@end
