import Footer from '@/components/Footer';
import {LockOutlined,  UserOutlined,} from '@ant-design/icons';
import {LoginForm,  ProFormText,} from '@ant-design/pro-components';
import { message} from 'antd';
import React from 'react';
import {FormattedMessage, history,  SelectLang, useIntl} from 'umi';
import styles from './index.less';
import {SYSTEM_LOGO} from "@/constants";
import {register} from "@/services/ant-design-pro/api";



const Login: React.FC = () => {

  const intl = useIntl();


  const handleSubmit = async (values: API.RegisterParams) => {
    //校验
    const {userPassword,checkPassword}=values;
    if (userPassword!==checkPassword){
      message.error('两次密码输入不一致');
      return;
    }
    try {
      // 注册
      const user = await register({ ...values });
      if (user) {
        const defaultLoginSuccessMessage = intl.formatMessage({
          id: 'pages.Register.success',
          defaultMessage: '注册成功！',
        });
        message.success(defaultLoginSuccessMessage);
        /** 此方法会跳转到 redirect 参数所在的位置  用来重定向到之前未登录时访问的页面*/
        if (!history) return;
        const { query } = history.location;
        history.push({
          pathname: '/user/login',
          query
        });
        return;
      }
    } catch (error : any) {
      const defaultLoginFailureMessage = intl.formatMessage({
        id: 'pages.Register.failure',
        defaultMessage: '注册失败，请重试！',
      });
      message.error(defaultLoginFailureMessage);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.lang} data-lang>
        {SelectLang && <SelectLang />}
      </div>
      <div className={styles.content}>
        <LoginForm
          submitter={{
            searchConfig: {
              submitText: '注册'
            }
            }}

          logo={<img alt="logo" src={SYSTEM_LOGO} />}
          title="用户中心"
          //subTitle={intl.formatMessage({ id: '最好的用管理系统' })}
          subTitle={<a href={""} >最好用的用户管理系统</a> }
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.RegisterParams);
          }}
        >

          {(
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined className={styles.prefixIcon} />,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.username.placeholder',
                  defaultMessage: '请输入账号',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.username.required"
                        defaultMessage="账号是必填项!"
                      />
                    ),
                  },
                  {
                    min:4,
                    message:'账号长度不能小于4位',
                    type:'string'
                  }
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon} />,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.password.placeholder',
                  defaultMessage: '请输入密码',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.password.required"
                        defaultMessage="密码是必填项！"
                      />
                    ),
                  },
                  {
                    min:6,
                    message:'密码长度不能小于6位',
                    type:'string'
                  }
                ]}
              />
              <ProFormText.Password
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon} />,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.password.placeholder',
                  defaultMessage: '请再次输入密码',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.password.required"
                        defaultMessage="确认密码是必填项！"
                      />
                    ),
                  },
                  {
                    min:6,
                    message:'密码长度不能小于6位',
                    type:'string'
                  }
                ]}
              />
              {/*扩展手机号*/}
              {/*<ProFormText*/}
              {/*  fieldProps={{*/}
              {/*    size: 'large',*/}
              {/*    prefix: <MobileOutlined className={styles.prefixIcon} />,*/}
              {/*  }}*/}
              {/*  name="mobile"*/}
              {/*  placeholder={intl.formatMessage({*/}
              {/*    id: 'pages.login.phoneNumber.placeholder',*/}
              {/*    defaultMessage: '请输入手机号',*/}
              {/*  })}*/}
              {/*  rules={[*/}
              {/*    {*/}
              {/*      required: true,*/}
              {/*      message: (*/}
              {/*        <FormattedMessage*/}
              {/*          id="pages.login.phoneNumber.required"*/}
              {/*          defaultMessage="手机号是必填项！"*/}
              {/*        />*/}
              {/*      ),*/}
              {/*    },*/}
              {/*    {*/}
              {/*      pattern: /^1\d{10}$/,*/}
              {/*      message: (*/}
              {/*        <FormattedMessage*/}
              {/*          id="pages.login.phoneNumber.invalid"*/}
              {/*          defaultMessage="手机号格式错误！"*/}
              {/*        />*/}
              {/*      ),*/}
              {/*    },*/}
              {/*  ]}*/}
              {/*/>*/}

              {/*<ProFormCaptcha*/}
              {/*  fieldProps={{*/}
              {/*    size: 'large',*/}
              {/*    prefix: <LockOutlined className={styles.prefixIcon} />,*/}
              {/*  }}*/}
              {/*  captchaProps={{*/}
              {/*    size: 'large',*/}
              {/*  }}*/}
              {/*  placeholder={intl.formatMessage({*/}
              {/*    id: 'pages.login.captcha.placeholder',*/}
              {/*    defaultMessage: '请输入验证码',*/}
              {/*  })}*/}
              {/*  captchaTextRender={(timing, count) => {*/}
              {/*    if (timing) {*/}
              {/*      return `${count} ${intl.formatMessage({*/}
              {/*        id: 'pages.getCaptchaSecondText',*/}
              {/*        defaultMessage: '获取验证码',*/}
              {/*      })}`;*/}
              {/*    }*/}
              {/*    return intl.formatMessage({*/}
              {/*      id: 'pages.login.phoneLogin.getVerificationCode',*/}
              {/*      defaultMessage: '获取验证码',*/}
              {/*    });*/}
              {/*  }}*/}
              {/*  name="captcha"*/}
              {/*  rules={[*/}
              {/*    {*/}
              {/*      required: true,*/}
              {/*      message: (*/}
              {/*        <FormattedMessage*/}
              {/*          id="pages.login.captcha.required"*/}
              {/*          defaultMessage="请输入验证码！"*/}
              {/*        />*/}
              {/*      ),*/}
              {/*    },*/}
              {/*  ]}*/}
              {/*  onGetCaptcha={async (phone) => {*/}
              {/*    const result = await getFakeCaptcha({*/}
              {/*      phone,*/}
              {/*    });*/}
              {/*    if (result === false) {*/}
              {/*      return;*/}
              {/*    }*/}
              {/*    message.success('获取验证码成功！验证码为：1234');*/}
              {/*  }}*/}
              {/*/>*/}
            </>
          )}

          <div
            style={{
              marginBottom: 24,
            }}
          >
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
